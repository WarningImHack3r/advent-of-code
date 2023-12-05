package main

import (
	"fmt"
	"strconv"
	"strings"
)

type Map struct {
	lines []MapLine
}

type MapLine struct {
	destinationRangeStart int
	sourceRangeStart      int
	rangeLength           int
}

func processMap(m Map, in int) (out int) {
	// Find the source range for the input 'in'
	for _, line := range m.lines {
		if in >= line.sourceRangeStart && in < line.sourceRangeStart+line.rangeLength {
			index := in - line.sourceRangeStart
			// On the same line, find the destination range from the source index
			return line.destinationRangeStart + index
		}
	}
	return in
}

func (d *Day) Day5(input []string) {
	seeds := make([]int, 0)
	maps := make([]Map, 0)

	// Parse input
	for i, line := range input {
		// Parsing seeds
		if strings.HasPrefix(line, "seeds: ") {
			for i, seedStr := range strings.Split(line, " ")[1:] {
				seed, _ := strconv.Atoi(seedStr)
				// Part 2: Every odd seed is a range from the previous seed included
				if i%2 == 1 {
					prevSeed := seeds[len(seeds)-1]
					for j := prevSeed + 1; j < prevSeed + seed; j++ {
						seeds = append(seeds, j)
					}
					continue
				}
				seeds = append(seeds, seed)
			}
			continue
		}

		// Parsing maps
		if line == "" {
			continue
		}
		if strings.HasSuffix(line, " map:") {
			nextIndex := i + 1
			newMap := Map{}

			for nextIndex < len(input) && input[nextIndex] != "" {
				splits := strings.Split(input[nextIndex], " ")

				destRangeStart, _ := strconv.Atoi(splits[0])
				sourceRangeStart, _ := strconv.Atoi(splits[1])
				rangeLength, _ := strconv.Atoi(splits[2])

				newMap.lines = append(newMap.lines, MapLine{
					destinationRangeStart: destRangeStart,
					sourceRangeStart:      sourceRangeStart,
					rangeLength:           rangeLength,
				})

				nextIndex++
			}

			maps = append(maps, newMap)
		}
	}

	// Solve
	lowestLocation := 0
	for _, seed := range seeds {
		nextMapInput := seed
		for _, m := range maps {
			nextMapInput = processMap(m, nextMapInput)
		}
		if nextMapInput < lowestLocation || lowestLocation == 0 {
			lowestLocation = nextMapInput
		}
	}

	fmt.Println("Lowest location:", lowestLocation)
}
