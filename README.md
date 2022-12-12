# Advent of Code

Code of my attempts on [adventofcode.com](https://adventofcode.com)

## Random tips
- Each year is in its respective folder (seems obvious)
- Run the `inputs_downloader.py` script to automatically download your current's year puzzle inputs
  - **The script requires your `session` token (available from the website's cookies storage), stored as `SESSION_TOKEN` in a `.env` file at the project's root**
  - Run `python3 -m pip install -r requirements.txt` before first execution
  - It actually only downloads *available* inputs (won't try to download a still locked puzzle input) if current month is december, else it's gonna ask you the year you want
  - Also, it won't redownload an already existing input file
  - Inputs will be placed under `[year]/inputs/input[day].txt`
- Don't forget to run `npm i` after clone to be able to run `.ts` files
- Command to run any `day[day].ts` code: `npm run start -- /path/to/day[day].ts`
  - Use the alternative `npm run watch [...]` command to rerun the file automatically after each change
