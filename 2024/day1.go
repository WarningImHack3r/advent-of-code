package main

import (
	"math"
	"slices"
	"strconv"
	"strings"
)

func (d *Day) Day1(input []string) {
	leftList := make([]int, len(input))
	rightList := make([]int, len(input))

	// Parsing
	for i, line := range input {
		columns := strings.Fields(line)
		if len(columns) != 2 {
			panic("There should be two columns!")
		}
		left, err := strconv.Atoi(columns[0])
		if err != nil {
			panic(err)
		}
		right, err := strconv.Atoi(columns[1])
		if err != nil {
			panic(err)
		}
		leftList[i] = left
		rightList[i] = right
	}

	// Sorting
	slices.Sort(leftList)
	slices.Sort(rightList)

	// Part 1
	var sum int
	for i, leftNum := range leftList {
		sum += int(math.Abs(float64(leftNum - rightList[i])))
	}

	d.SetPart1Answer(sum)

	// Part 2
	sum = 0
	for _, leftNum := range slices.Compact(leftList) {
		var occurrences int
		for _, rightNum := range rightList {
			if leftNum > rightNum {
				continue
			}
			if leftNum == rightNum {
				occurrences++
				continue
			}
			break
		}
		sum += leftNum * occurrences
	}

	d.SetPart2Answer(sum)
}
