package main

import (
	"slices"
	"testing"
)

var (
	originalGalaxy = []string{
		"...#......",
		".......#..",
		"#.........",
		"..........",
		"......#...",
		".#........",
		".........#",
		"..........",
		".......#..",
		"#...#.....",
	}
	fullGalaxy = []string{
		"....#........",
		".........#...",
		"#............",
		".............",
		".............",
		"........#....",
		".#...........",
		"............#",
		".............",
		".............",
		".........#...",
		"#....#.......",
	}
)

func TestExpanding(t *testing.T) {
	verticallyExpandedGalaxy := []string{
		"...#......",
		".......#..",
		"#.........",
		"..........",
		"..........",
		"......#...",
		".#........",
		".........#",
		"..........",
		"..........",
		".......#..",
		"#...#.....",
	}
	horizontallyExpandedGalaxy := []string{
		"....#........",
		".........#...",
		"#............",
		".............",
		"........#....",
		".#...........",
		"............#",
		".............",
		".........#...",
		"#....#.......",
	}

	t.Run(
		"Expanding the universe vertically", func(t *testing.T) {
			expandedGalaxy := expandGalaxyVertically(originalGalaxy, defaultGalaxyLineMatchHandler)
			if len(expandedGalaxy) != len(verticallyExpandedGalaxy) {
				t.Errorf(
					"Expected vertically expanded galaxy to have %d lines, got %d", len(verticallyExpandedGalaxy),
					len(expandedGalaxy),
				)
			}
			for i, line := range expandedGalaxy {
				if line != verticallyExpandedGalaxy[i] {
					t.Errorf(
						"Expected line %d to be %q (%d long), got %q (%d long)", i, verticallyExpandedGalaxy[i],
						len(verticallyExpandedGalaxy[i]), line, len(line),
					)
				}
			}
		},
	)

	t.Run(
		"Expanding the universe horizontally", func(t *testing.T) {
			expandedGalaxy := expandGalaxyHorizontally(originalGalaxy, defaultGalaxyColumnMatchHandler)
			if len(expandedGalaxy) != len(horizontallyExpandedGalaxy) {
				t.Errorf(
					"Expected horizontally expanded galaxy to have %d lines, got %d", len(horizontallyExpandedGalaxy),
					len(expandedGalaxy),
				)
			}
			for i, line := range expandedGalaxy {
				if line != horizontallyExpandedGalaxy[i] {
					t.Errorf(
						"Expected line %d to be %q (%d long), got %q (%d long)", i, horizontallyExpandedGalaxy[i],
						len(horizontallyExpandedGalaxy[i]), line, len(line),
					)
				}
			}
		},
	)

	t.Run(
		"Expanding the universe both vertically and horizontally", func(t *testing.T) {
			expandedGalaxy := expandGalaxyHorizontally(
				expandGalaxyVertically(
					originalGalaxy, defaultGalaxyLineMatchHandler,
				), defaultGalaxyColumnMatchHandler,
			)
			if len(expandedGalaxy) != len(fullGalaxy) {
				t.Errorf(
					"Expected fully expanded galaxy to have %d lines, got %d", len(fullGalaxy),
					len(expandedGalaxy),
				)
			}
			for i, line := range expandedGalaxy {
				if line != fullGalaxy[i] {
					t.Errorf(
						"Expected line %d to be %q (%d long), got %q (%d long)", i, fullGalaxy[i], len(fullGalaxy[i]),
						line, len(line),
					)
				}
			}
		},
	)

	t.Run(
		"Expanding the universe in both directions", func(t *testing.T) {
			expandedGalaxy := expandGalaxyHorizontally(
				expandGalaxyVertically(
					originalGalaxy, defaultGalaxyLineMatchHandler,
				), defaultGalaxyColumnMatchHandler,
			)
			otherExpandedGalaxy := expandGalaxyVertically(
				expandGalaxyHorizontally(
					originalGalaxy, defaultGalaxyColumnMatchHandler,
				), defaultGalaxyLineMatchHandler,
			)
			if len(expandedGalaxy) != len(otherExpandedGalaxy) {
				t.Errorf(
					"Expected fully expanded galaxy to have %d lines, got %d", len(otherExpandedGalaxy),
					len(expandedGalaxy),
				)
			}
			for i, line := range expandedGalaxy {
				if line != otherExpandedGalaxy[i] {
					t.Errorf(
						"Expected line %d to be %q (%d long), got %q (%d long)", i, otherExpandedGalaxy[i],
						len(otherExpandedGalaxy[i]), line, len(line),
					)
				}
			}
		},
	)
}

func TestExpandingMatching(t *testing.T) {
	t.Run(
		"Row expansion matching", func(t *testing.T) {
			var matchingLines []int
			expandGalaxyVertically(
				originalGalaxy, func(galaxy []string, line int, _ int) []string {
					matchingLines = append(matchingLines, line)
					return galaxy
				},
			)
			expectedMatchingLines := []int{3, 7}
			if !slices.Equal(matchingLines, expectedMatchingLines) {
				t.Errorf("Expected matching lines to be %v, got %v", expectedMatchingLines, matchingLines)
			}
		},
	)

	t.Run(
		"Column expansion matching", func(t *testing.T) {
			var matchingColumns []int
			expandGalaxyHorizontally(
				originalGalaxy, func(galaxy []string, column int) []string {
					matchingColumns = append(matchingColumns, column)
					return galaxy
				},
			)
			expectedMatchingColumns := []int{2, 5, 8}
			if !slices.Equal(matchingColumns, expectedMatchingColumns) {
				t.Errorf("Expected matching columns to be %v, got %v", expectedMatchingColumns, matchingColumns)
			}
		},
	)
}

func TestGalaxyCount(t *testing.T) {
	galaxies := findGalaxies(fullGalaxy, nil, nil, 0)
	if len(galaxies) != 9 {
		t.Errorf("Expected 9 galaxies, got %d", len(galaxies))
	}
}

func TestIndividualDistances(t *testing.T) {
	galaxies := findGalaxies(fullGalaxy, nil, nil, 0)

	t.Run(
		"Distance between galaxies 5 and 9", func(t *testing.T) {
			if distanceBetween(galaxies[4], galaxies[8]) != 9 {
				t.Errorf(
					"Expected distance between galaxies 4 and 8 to be 9, got %d",
					distanceBetween(galaxies[4], galaxies[8]),
				)
			}
		},
	)

	t.Run(
		"Distance between galaxies 1 and 7", func(t *testing.T) {
			if distanceBetween(galaxies[0], galaxies[6]) != 15 {
				t.Errorf(
					"Expected distance between galaxies 0 and 6 to be 15, got %d",
					distanceBetween(galaxies[0], galaxies[6]),
				)
			}
		},
	)

	t.Run(
		"Distance between galaxies 3 and 6", func(t *testing.T) {
			if distanceBetween(galaxies[2], galaxies[5]) != 17 {
				t.Errorf(
					"Expected distance between galaxies 2 and 5 to be 17, got %d",
					distanceBetween(galaxies[2], galaxies[5]),
				)
			}
		},
	)

	t.Run(
		"Distance between galaxies 8 and 9", func(t *testing.T) {
			if distanceBetween(galaxies[7], galaxies[8]) != 5 {
				t.Errorf(
					"Expected distance between galaxies 7 and 8 to be 5, got %d",
					distanceBetween(galaxies[7], galaxies[8]),
				)
			}
		},
	)
}

func TestFullVSExpandedGalaxies(t *testing.T) {
	galaxies := findGalaxies(fullGalaxy, nil, nil, 0)
	var expandedLines []int
	bigGalaxy := expandGalaxyVertically(
		originalGalaxy, func(galaxy []string, line, _ int) []string {
			expandedLines = append(expandedLines, line)
			return galaxy
		},
	)
	var expandedColumns []int
	bigGalaxy = expandGalaxyHorizontally(
		originalGalaxy, func(galaxy []string, column int) []string {
			expandedColumns = append(expandedColumns, column)
			return galaxy
		},
	)
	farAwayGalaxies := findGalaxies(bigGalaxy, expandedLines, expandedColumns, 1)

	if len(farAwayGalaxies) != len(galaxies) {
		t.Fatalf("Expected %d galaxies, got %d", len(galaxies), len(farAwayGalaxies))
	}
	for i, farAwayGalaxy := range farAwayGalaxies {
		if farAwayGalaxy != galaxies[i] {
			t.Errorf("Expected galaxy %d to be %+v, got %+v", i+1, galaxies[i], farAwayGalaxy)
		}
	}
}

func TestFullDistances(t *testing.T) {
	galaxies := findGalaxies(fullGalaxy, nil, nil, 0)
	pairs, sum := computeDistancesBetweenGalaxies(galaxies)
	if pairs != 36 {
		t.Errorf("Expected 36 pairs, got %d", pairs)
	}
	if sum != 374 {
		t.Errorf("Expected sum of distances to be 374, got %d", sum)
	}
}

func TestFullFledged(t *testing.T) {
	t.Run(
		"Full-fledged test", func(t *testing.T) {
			d := Day{}
			d.Day11(originalGalaxy)
			if d.part1 != nil && *d.part1 != 374 {
				t.Errorf("Expected part 1 to be 374, got %d", *d.part1)
			}
		},
	)
}
