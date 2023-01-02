"""Download inputs from adventofcode.com"""
from datetime import datetime
import os
import sys
import requests
from dotenv import load_dotenv
import pytz

load_dotenv()

SESSION = os.getenv("SESSION_TOKEN")

# Download file from url with cookie
def download_input(year: str, day: str, cookie: str, filename: str):
    """Download file from url with cookie"""
    cookies = {"session": cookie}
    url = f"https://adventofcode.com/{year}/day/{day}/input"
    print(f"Downloading {url} to {filename}...")
    request = requests.get(url, cookies=cookies, stream=True, timeout=10)
    request.raise_for_status()
    with open(filename, "wb") as file:
        for chunk in request.iter_content(chunk_size=1024):
            if chunk:
                file.write(chunk)
                file.flush()
        print(f"Downloaded day {day} of {year} to {filename}")

if __name__ == "__main__":
    # Check if session token is set
    if not SESSION:
        print("Please set SESSION_TOKEN in .env file")
        sys.exit(1)
    # Get time in adventofcode's timezone
    time = datetime.now(pytz.timezone("EST"))
    # If it's december, download inputs for the current year, otherwise ask for year
    base_year = time.year
    if time.month < 12:
        wanted_year = input("Which year do you want to download? ")
        if wanted_year:
            base_year = int(wanted_year)
            if base_year < 2015 or base_year >= time.year:
                if base_year == time.year:
                    print("It's not december yet! " + \
                        f"Please enter a year between 2015 and {time.year - 1}")
                else:
                    print(f"Invalid year, please enter a year between 2015 and {time.year - 1}")
                sys.exit(1)
    # Loop over days and download inputs
    for i in range(1, min(26, time.day + 1) if time.month == 12 else 26):
        dest_path = f"{base_year}/inputs/input{i}.txt"
        if not os.path.exists(dest_path):
            download_input(str(base_year), str(i), SESSION, dest_path)
        else:
            print(f"Skipping day {i} of {base_year} because it already exists")
