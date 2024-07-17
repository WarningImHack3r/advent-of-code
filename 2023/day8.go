package main

import "strings"

type Direction uint

const (
	LEFT Direction = iota
	RIGHT
)

type DirectionManager struct {
	Pattern string
	index   uint
}

func (m *DirectionManager) NextDirection() Direction {
	if m.index >= uint(len(m.Pattern)) {
		m.index = 0
	}
	dir := strings.ToUpper(m.Pattern)[m.index]
	m.index++
	if dir == 'L' {
		return LEFT
	}
	return RIGHT
}

type Mapping struct {
	leftValue  string
	rightValue string
}

func (m *Mapping) destinationAt(dir Direction) string {
	if dir == LEFT {
		return m.leftValue
	}
	return m.rightValue
}

func (d *Day) Day8(input []string) {
	var manager DirectionManager
	mappings := map[string]Mapping{}

	// Parsing input
	for _, line := range input {
		if manager.Pattern == "" {
			manager.Pattern = line
			continue
		}

		if line == "" {
			continue
		}

		mappingLine := strings.Split(line, " = ")
		values := strings.Split(mappingLine[1], ", ")
		mapping := Mapping{
			leftValue:  strings.Replace(values[0], "(", "", 1),
			rightValue: strings.Replace(values[1], ")", "", 1),
		}
		mappings[mappingLine[0]] = mapping
	}

	position := mappings["AAA"]
	counter := 0
	for position != mappings["ZZZ"] {
		direction := manager.NextDirection()
		position = mappings[position.destinationAt(direction)]
		counter++
	}

	// step 1
	d.SetPart1Answer(counter)

	positions := make([]string, 0, len(mappings))
	for k := range mappings {
		if strings.HasSuffix(k, "A") {
			positions = append(positions, k)
		}
	}

	counter = 0
	loopPoints := make([][]int, len(positions))
	diffs := make([]int, len(positions))
	var direction Direction
infinite:
	for {
		counter++
		direction = manager.NextDirection()
		for i, pos := range positions {
			mapping := mappings[pos]
			positions[i] = mapping.destinationAt(direction)
			if strings.HasSuffix(pos, "Z") {
				loopPoints[i] = append(loopPoints[i], counter)
			}
		}
		for i, point := range loopPoints {
			if len(point) == 2 {
				diffs[i] = point[1] - point[0]
			}
		}
		for _, diff := range diffs {
			if diff == 0 {
				continue infinite
			}
		}
		break
	}

	// step 2
	d.SetPart2Answer(LCM(diffs...))
}

func GCD(a, b int) int {
	for b != 0 {
		a, b = b, a%b
	}
	return a
}

func LCM(integers ...int) int {
	if len(integers) < 2 {
		return 0
	}
	a := integers[0]
	b := integers[1]
	result := a * b / GCD(a, b)

	for _, i := range integers[2:] {
		result = LCM(result, i)
	}

	return result
}
