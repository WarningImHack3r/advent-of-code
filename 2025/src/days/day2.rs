pub fn answers(input: &str) -> (i64, i64) {
    let ranges = input.trim().split(",").map(|raw| {
        let (a, b) = raw.split_once("-").unwrap();
        let beginning = a.parse::<i64>().unwrap();
        let end = b.parse::<i64>().unwrap();
        beginning..=end
    });

    let mut sum1 = 0;
    let mut sum2 = 0;
    for range in ranges {
        for i in range {
            let str_version = i.to_string();
            let num_size = str_version.len();
            let half_size = num_size / 2;
            for s in (1..=half_size).rev() {
                let chunks = str_version
                    .chars()
                    .collect::<Vec<char>>()
                    .chunks(s)
                    .map(|c| c.iter().collect::<String>())
                    .collect::<Vec<String>>();
                let all_equal = match chunks.iter().as_slice() {
                    [head, tail @ ..] => tail.iter().all(|x| x == head),
                    _ => false,
                };
                if all_equal {
                    sum2 += i;
                    if chunks.len() == 2 {
                        sum1 += i;
                    }
                    break;
                }
            }
        }
    }

    (sum1, sum2)
}

#[cfg(test)]
mod tests_day02 {
    use super::*;

    const EXAMPLE_INPUT: &str = "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124";

    #[test]
    fn test_part1_example() {
        let (part1, _) = answers(EXAMPLE_INPUT);
        assert_eq!(part1, 1_227_775_554);
    }

    #[test]
    fn test_part2_example() {
        let (_, part2) = answers(EXAMPLE_INPUT);
        assert_eq!(part2, 4_174_379_265);
    }
}
