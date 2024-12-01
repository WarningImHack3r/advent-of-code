"""Download inputs from adventofcode.com"""
from datetime import datetime
import os
import sys
import requests
from dotenv import load_dotenv
import pytz

load_dotenv()

SESSION = os.getenv("SESSION_TOKEN")

def download_input(year: int, day: int, session_token: str, filename: str):
    """
    Download an adventofcode input file from the year, day, token and
    destination file name.
    """
    cookies = {"session": session_token}
    url = f"https://adventofcode.com/{year}/day/{day}/input"
    print(f"Downloading {url} to {filename}...")
    request = requests.get(url, cookies=cookies, stream=True, timeout=10)
    try:
        request.raise_for_status()
    except requests.exceptions.HTTPError as e:
        match e.response.status_code:
            case 400:
                print(f"Bad Request, is your token (still) valid? ({e})")
                sys.exit(1)
            case _:
                raise e

    with open(filename, "wb") as file:
        for chunk in request.iter_content(chunk_size=1024):
            if chunk:
                file.write(chunk)
                file.flush()
        print(f"Downloaded day {day} of {year} to {filename}")


if __name__ == "__main__":
    # Check if the session token is set
    if not SESSION:
        print("Please set SESSION_TOKEN in .env file")
        sys.exit(1)
    # Get time in the timezone of adventofcode
    time = datetime.now(pytz.timezone("EST"))
    # If we're in december, download inputs for the current year, otherwise ask for year
    base_year = time.year
    aoc_creation_year = 2015
    if time.month < 12:
        wanted_year = input("Which year do you want to download? ")
        if wanted_year:
            base_year = int(wanted_year)
            if base_year < aoc_creation_year or base_year >= time.year:
                if base_year == time.year:
                    print("It's not december yet! " + \
                        f"Please enter a year between {aoc_creation_year} and {time.year - 1}")
                elif base_year < aoc_creation_year:
                    print(f"Advent of Code didn't exist in {base_year}, it was created in {aoc_creation_year} ^^")
                else:
                    print("I can't see into the future yet!! " + \
                          f"Please enter a year between {aoc_creation_year} and {time.year - 1}")
                sys.exit(1)
    # Create directories for `year/inputs` if they don't exist
    os.makedirs(f"{base_year}/inputs", exist_ok=True)
    # Loop over days and download inputs
    for i in range(1, min(26, time.day + 1) if time.month == 12 else 26):
        dest_path = f"{base_year}/inputs/input{i}.txt"
        if not os.path.exists(dest_path):
            download_input(base_year, i, SESSION, dest_path)
        else:
            print(f"Skipping day {i} of {base_year} because it already exists")
    print("Done! Happy coding!")
