package main

import (
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

func parseSeeds(input []string, maps *[]Map, seeds *[]int, spe func(int, int) bool) {
	for i, line := range input {
		// Parsing seeds
		if strings.HasPrefix(line, "seeds: ") {
			for j, seedStr := range strings.Split(line, " ")[1:] {
				seed, _ := strconv.Atoi(seedStr)
				if spe(j, seed) {
					continue
				}
				*seeds = append(*seeds, seed)
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

				newMap.lines = append(
					newMap.lines, MapLine{
						destinationRangeStart: destRangeStart,
						sourceRangeStart:      sourceRangeStart,
						rangeLength:           rangeLength,
					},
				)

				nextIndex++
			}

			*maps = append(*maps, newMap)
		}
	}
}

func findLowestLocation(maps []Map, seeds []int) (lowestLocation int) {
	for _, seed := range seeds {
		nextMapInput := seed
		for _, m := range maps {
			nextMapInput = processMap(m, nextMapInput)
		}
		if nextMapInput < lowestLocation || lowestLocation == 0 {
			lowestLocation = nextMapInput
		}
	}
	return
}

func (d *Day) Day5(input []string) {
	seeds := make([]int, 0)
	maps := make([]Map, 0)
	seeds2 := make([]int, 0)
	maps2 := make([]Map, 0)

	// Parse input
	parseSeeds(input, &maps, &seeds, func(j int, seed int) bool { return false })
	parseSeeds(
		input, &maps2, &seeds2, func(index int, seed int) bool {
			// Part 2: Every odd seed is a range from the previous seed included
			if index%2 == 1 {
				prevSeed := seeds2[len(seeds2)-1]
				for k := prevSeed + 1; k < prevSeed+seed; k++ {
					seeds2 = append(seeds2, k)
				}
				return true
			}
			return false
		},
	)

	// Solve
	d.SetPart1Answer(findLowestLocation(maps, seeds))
	d.SetPart2Answer(findLowestLocation(maps2, seeds2))
}
