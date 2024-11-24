package main

import (
	"math"
	"slices"
)

type Galaxy struct {
	x, y int
}

var (
	// defaultGalaxyLineMatchHandler is the default handler for when a line matches
	// the criteria for expansion.
	defaultGalaxyLineMatchHandler = func(galaxy []string, line, addedLines int) []string {
		lineStr := make([]byte, len(galaxy[0]))
		for i := range lineStr {
			lineStr[i] = '.'
		}
		return append(
			galaxy[:line+addedLines], append([]string{string(lineStr)}, galaxy[line+addedLines:]...)...,
		)
	}
	// defaultGalaxyColumnMatchHandler is the default handler for when a column matches
	// the criteria for expansion.
	defaultGalaxyColumnMatchHandler = func(galaxy []string, column int) []string {
		modifiedGalaxy := make([]string, len(galaxy))
		copy(modifiedGalaxy, galaxy)
		for i, line := range modifiedGalaxy {
			modifiedGalaxy[i] = line[:column] + "." + line[column:]
		}
		return modifiedGalaxy
	}
)

func expandGalaxyVertically(galaxy []string, onLineMatch func([]string, int, int) []string) []string {
	modifiedGalaxy := make([]string, len(galaxy))
	copy(modifiedGalaxy, galaxy)

	addedLines := 0
	for i, line := range modifiedGalaxy {
		isLineOnlyDots := true
		for _, c := range line {
			if c != '.' {
				isLineOnlyDots = false
				break
			}
		}
		if isLineOnlyDots {
			result := onLineMatch(modifiedGalaxy, i, addedLines)
			addedLines += len(result) - len(modifiedGalaxy)
			modifiedGalaxy = result
		}
	}
	return modifiedGalaxy
}

func expandGalaxyHorizontally(galaxy []string, onColumnMatch func([]string, int) []string) []string {
	modifiedGalaxy := make([]string, len(galaxy))
	copy(modifiedGalaxy, galaxy)
	skipNextColumnCount := 0
	for i := 0; i < len(modifiedGalaxy[0]); i++ {
		if skipNextColumnCount > 0 {
			skipNextColumnCount--
			continue
		}
		isColumnOnlyDots := true
		for _, line := range modifiedGalaxy {
			if line[i] != '.' {
				isColumnOnlyDots = false
				break
			}
		}
		if isColumnOnlyDots {
			result := onColumnMatch(modifiedGalaxy, i)
			skipNextColumnCount = len(result[0]) - len(modifiedGalaxy[0])
			modifiedGalaxy = result
		}
	}
	return modifiedGalaxy
}

func findGalaxies(
	galaxy []string, expandedLines, expandedColumns []int, expansionLength int,
) (galaxies []Galaxy) {
	additionalX := 0
	for x, line := range galaxy {
		if slices.Contains(expandedLines, x) {
			additionalX += expansionLength
		}
		additionalY := 0
		for y, c := range line {
			if slices.Contains(expandedColumns, y) {
				additionalY += expansionLength
			}
			if c == '#' {
				galaxies = append(
					galaxies, Galaxy{
						x: x + additionalX,
						y: y + additionalY,
					},
				)
			}
		}
	}
	return
}

func distanceBetween(a, b Galaxy) int {
	return int(math.Abs(float64(a.x-b.x)) + math.Abs(float64(a.y-b.y)))
}

func computeDistancesBetweenGalaxies(galaxies []Galaxy) (pairsCount, sum int) {
	for i, a := range galaxies {
		for j, b := range galaxies {
			if i >= j {
				// Don't compare a galaxy to itself, nor compare the same pair twice
				continue
			}
			pairsCount++
			sum += distanceBetween(a, b)
		}
	}
	return
}

func (d *Day) Day11(galaxy []string) {
	// Expand the universe vertically
	newGalaxy := expandGalaxyVertically(galaxy, defaultGalaxyLineMatchHandler)

	// Expand the universe horizontally
	newGalaxy = expandGalaxyHorizontally(newGalaxy, defaultGalaxyColumnMatchHandler)

	// Create galaxies
	galaxies := findGalaxies(newGalaxy, nil, nil, 0)

	// Find the sum of the distances between all galaxies, unique pairs
	_, sum := computeDistancesBetweenGalaxies(galaxies)
	d.SetPart1Answer(sum)

	var expandedLines []int
	bigGalaxy := expandGalaxyVertically(
		galaxy, func(g []string, line, _ int) []string {
			expandedLines = append(expandedLines, line)
			return g
		},
	)
	var expandedColumns []int
	bigGalaxy = expandGalaxyHorizontally(
		bigGalaxy, func(g []string, column int) []string {
			expandedColumns = append(expandedColumns, column)
			return g
		},
	)
	farAwayGalaxies := findGalaxies(bigGalaxy, expandedLines, expandedColumns, 1_000_000-1)

	_, sum = computeDistancesBetweenGalaxies(farAwayGalaxies)
	d.SetPart2Answer(sum)
}
