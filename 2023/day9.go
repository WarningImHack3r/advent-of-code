package main

import (
	"fmt"
	"strconv"
	"strings"
)

type PredictionKind int

const (
	Forward PredictionKind = iota
	Backward
)

func computeDifferences(sequence []int) []int {
	differences := make([]int, len(sequence)-1)
	for i := 1; i < len(sequence); i++ {
		differences[i-1] = sequence[i] - sequence[i-1]
	}
	return differences
}

func allZeroes(sequence []int) bool {
	for _, value := range sequence {
		if value != 0 {
			return false
		}
	}
	return true
}

func getPrediction(sequence []int, kind PredictionKind) int {
	if allZeroes(sequence) {
		return 0
	}
	diffs := computeDifferences(sequence)
	prediction := getPrediction(diffs, kind)
	switch kind {
	case Forward:
		return sequence[len(sequence)-1] + prediction
	case Backward:
		return sequence[0] - prediction
	}
	panic("unreachable")
}

func (d *Day) Day9(input []string) {
	var sum int
	var backwardSum int

	for _, line := range input {
		// Parse input
		split := strings.Split(line, " ")
		lineNumbers := make([]int, len(split))
		for i, n := range strings.Split(line, " ") {
			parsed, err := strconv.Atoi(n)
			if err != nil {
				panic(err)
			}
			lineNumbers[i] = parsed
		}

		// Compute it
		sum += getPrediction(lineNumbers, Forward)
		backwardSum += getPrediction(lineNumbers, Backward)
	}

	// Print the result
	fmt.Println("Part 1:", sum)
	fmt.Println("Part 2:", backwardSum)
}
