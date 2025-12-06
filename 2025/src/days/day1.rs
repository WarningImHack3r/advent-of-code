pub fn answers(input: &str) -> (i32, i32) {
    let moves = input.trim().lines().map(|line| {
        let letter = line.chars().nth(0).unwrap();
        let count = line.get(1..).unwrap().parse::<i32>().unwrap();
        match letter {
            'L' => -count,
            _ => count,
        }
    });

    let mut zeroes = 0;
    let mut wraps = 0;
    let mut index = 50;
    for mov in moves {
        let diff = index + mov;
        wraps += match mov {
            // stole ONLY THIS MATCH on GH because I was going crazy sorry...
            // https://github.com/yi-json/aoc-2025/blob/main/src/bin/day01.rs#L71
            // I was VERY CLOSE with `diff.div_euclid(100).abs()`
            // did I mention I suck at math?
            mov if mov < 0 => (index - 1).div_euclid(100) - (diff - 1).div_euclid(100),
            mov if mov > 0 => diff.div_euclid(100) - index.div_euclid(100),
            _ => 0,
        };
        index = diff.rem_euclid(100);
        if index == 0 {
            zeroes += 1;
        }
    }

    (zeroes, wraps)
}

#[cfg(test)]
mod tests_day01 {
    use super::*;

    const EXAMPLE_INPUT: &str = "\
L68
L30
R48
L5
R60
L55
L1
L99
R14
L82
";

    #[test]
    fn test_part1_example() {
        let (part1, _) = answers(EXAMPLE_INPUT);
        assert_eq!(part1, 3);
    }

    #[test]
    fn test_part2_example() {
        let (_, part2) = answers(EXAMPLE_INPUT);
        assert_eq!(part2, 6);
    }
}
