use std::{env, fs};

use aoc2025::days;

fn read_file(day: u8) -> String {
    let inputs_file = env::current_dir()
        .unwrap()
        .join("inputs")
        .join(format!("input{day}.txt"));
    fs::read_to_string(inputs_file).expect("could not open input file")
}

fn main() {
    const DAY: u8 = 2;
    let file = read_file(DAY);
    let (part1, part2) = days::day2::answers(&file);
    println!("Day {DAY} part 1: {part1}");
    println!("Day {DAY} part 2: {part2}");
}
