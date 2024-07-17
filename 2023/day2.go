package main

import (
	"strconv"
	"strings"
)

const (
	MaxRed   = 12
	MaxGreen = 13
	MaxBlue  = 14
)

func isGamePossible(sets []string) bool {
	lineIsOk := true
	for _, series := range sets {
		for _, color := range strings.Split(series, ", ") {
			colorCount := strings.Split(color, " ")
			strNb, err := strconv.Atoi(colorCount[0])
			if err != nil {
				panic(err)
			}

			if colorCount[1] == "red" {
				if strNb > MaxRed {
					lineIsOk = false
					break
				}
			} else if colorCount[1] == "green" {
				if strNb > MaxGreen {
					lineIsOk = false
					break
				}
			} else if colorCount[1] == "blue" {
				if strNb > MaxBlue {
					lineIsOk = false
					break
				}
			}
		}
	}

	return lineIsOk
}

func linePower(sets []string) int {
	minRed := 1
	minGreen := 1
	minBlue := 1
	for _, series := range sets {
		for _, color := range strings.Split(series, ", ") {
			colorCount := strings.Split(color, " ")
			strNb, err := strconv.Atoi(colorCount[0])
			if err != nil {
				panic(err)
			}

			if colorCount[1] == "red" {
				minRed = max(minRed, strNb)
			} else if colorCount[1] == "green" {
				minGreen = max(minGreen, strNb)
			} else if colorCount[1] == "blue" {
				minBlue = max(minBlue, strNb)
			}
		}
	}

	return minRed * minGreen * minBlue
}

func (d *Day) Day2(input []string) {
	var possibleIdsSum int
	var setsPowerSum int
	for _, line := range input {
		parts := strings.Split(line, ": ")
		gameId, err := strconv.Atoi(strings.Split(parts[0], " ")[1])
		if err != nil {
			panic(err)
		}

		sets := strings.Split(parts[1], "; ")
		if isGamePossible(sets) {
			possibleIdsSum += gameId
		}
		setsPowerSum += linePower(sets)
	}

	d.SetPart1Answer(possibleIdsSum)
	d.SetPart2Answer(setsPowerSum)
}
