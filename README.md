# Advent of Code

Code of my attempts on [adventofcode.com](https://adventofcode.com)

## Random tips

- Each year is in its respective folder (seems obvious)
- Run the `inputs_downloader.py` script to automatically download your current's year puzzle inputs
  - **The script requires your `session` token (available from the website's cookies storage), stored as `SESSION_TOKEN` in a `.env` file at the project's root**
  - Run `python3 -m pip install -r requirements.txt` before first execution
  - It only downloads *available* inputs (won't try to download a still locked puzzle input) if the current month is December, else it's going to ask you the year you want
  - Also, it won't re-download an already existing input file
  - Inputs will be placed under `[year]/inputs/input[day].txt`

### Additional tips for TypeScript

- Command to run any `day[day].ts` code: `pnpm start -- /path/to/day[day].ts`
  - Use the alternative `pnpm run watch [...]` command to rerun the file automatically after each change
