package main

import (
	"fmt"
	"strconv"
	"strings"
)

func (d *Day) Day6(input []string) {
	times := make([]int, 0)
	concatTime := 0
	distances := make([]int, 0)
	concatDistance := 0

	// Parse input
	for _, line := range input {
		if strings.HasPrefix(line, "Time:") {
			timesStr := strings.Fields(line[5:])
			strDigits := ""
			for _, timeStr := range timesStr {
				strDigits += timeStr
				time, err := strconv.Atoi(timeStr)
				if err != nil {
					fmt.Printf("Error converting time to int: %v", err)
				}
				times = append(times, time)
			}
			concatTime, _ = strconv.Atoi(strDigits)
		} else if strings.HasPrefix(line, "Distance:") {
			distancesStr := strings.Fields(line[9:])
			strDigits := ""
			for _, distanceStr := range distancesStr {
				strDigits += distanceStr
				distance, err := strconv.Atoi(distanceStr)
				if err != nil {
					fmt.Printf("Error converting distance to int: %v", err)
				}
				distances = append(distances, distance)
			}
			concatDistance, _ = strconv.Atoi(strDigits)
		}
	}

	// Solve part 1
	errorMargin := 1
	for i := 0; i < len(times); i++ {
		recordDistance := distances[i]
		maxTime := times[i]

		winPossibilities := 0
		for j := 0; j <= maxTime; j++ {
			distance := (maxTime - j) * j
			if distance > recordDistance {
				winPossibilities++
			}
		}
		errorMargin *= winPossibilities
	}

	fmt.Printf("[Step 1] Error margin: %v\n", errorMargin)

	// Solve part 2
	errorMargin = 0
	for i := 0; i <= concatTime; i++ {
		distance := (concatTime - i) * i
		if distance > concatDistance {
			errorMargin++
		}
	}

	fmt.Printf("[Step 2] Error margin: %v\n", errorMargin)
}
