package main

import (
	"fmt"
	"strconv"
	"strings"
)

func replacedLetters(s string) string {
	s = strings.ReplaceAll(s, "eightwo", "82")
	s = strings.ReplaceAll(s, "twone", "21")
	s = strings.ReplaceAll(s, "oneight", "18")
	// s = strings.ReplaceAll(s, "fiveight", "5")
	// s = strings.ReplaceAll(s, "sevenine", "7")
	// s = strings.ReplaceAll(s, "eighthree", "8")
	// s = strings.ReplaceAll(s, "nineight", "9")
	// s = strings.ReplaceAll(s, "threeight", "3")

	s = strings.ReplaceAll(s, "one", "1")
	s = strings.ReplaceAll(s, "two", "2")
	s = strings.ReplaceAll(s, "three", "3")
	s = strings.ReplaceAll(s, "four", "4")
	s = strings.ReplaceAll(s, "five", "5")
	s = strings.ReplaceAll(s, "six", "6")
	s = strings.ReplaceAll(s, "seven", "7")
	s = strings.ReplaceAll(s, "eight", "8")
	s = strings.ReplaceAll(s, "nine", "9")

	return s
}

func removeLetters(s string) string {
	var newS string
	for _, char := range s {
		numchar := int(char)
		if numchar >= 48 && numchar <= 57 {
			newS += string(char)
		}
	}
	return newS
}

func (d *Day) Day1(input []string) {
	var sum int
	for _, line := range input {
		// part 2: replace lettered numbers with their actual values
		line = replacedLetters(line)

		// remove all letters from the string
		line = removeLetters(line)

		// remove the middle digits if len(line) > 2, otherwise duplicate the first digit
		if len(line) >= 2 {
			line = line[:1] + line[len(line)-1:]
		} else {
			if len(line) == 0 {
				continue
			}
			line = line[:1] + line[:1]
		}

		// convert the string to an int and add it to the sum
		num, err := strconv.Atoi(line)
		if err != nil {
			panic(err)
		}
		sum += num
	}

	fmt.Println(sum)
}
