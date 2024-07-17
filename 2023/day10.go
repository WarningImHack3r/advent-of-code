package main

import (
	"fmt"
	"slices"
)

type Pipe struct {
	pattern                  string
	x, y                     int
	north, south, east, west *Pipe
	visited                  bool
}

func pipeAtCoordinatesOrNil(pipesMap [][]*Pipe, i, j int, patterns ...string) *Pipe {
	if i < 0 || i >= len(pipesMap) || j < 0 || j >= len(pipesMap[i]) {
		return nil
	}
	destPipe := pipesMap[i][j]
	if destPipe != nil && slices.Contains(patterns, destPipe.pattern) {
		return destPipe
	}
	return nil
}

func deepestNodesList(startingNode *Pipe) []*Pipe {
	if startingNode == nil {
		// Never happens
		return []*Pipe{}
	}

	startingNode.visited = true

	visitedNodes := []*Pipe{startingNode}
	if startingNode.east != nil && !startingNode.east.visited {
		visitedNodes = append(visitedNodes, deepestNodesList(startingNode.east)...)
	}
	if startingNode.south != nil && !startingNode.south.visited {
		visitedNodes = append(visitedNodes, deepestNodesList(startingNode.south)...)
	}
	if startingNode.west != nil && !startingNode.west.visited {
		visitedNodes = append(visitedNodes, deepestNodesList(startingNode.west)...)
	}
	if startingNode.north != nil && !startingNode.north.visited {
		visitedNodes = append(visitedNodes, deepestNodesList(startingNode.north)...)
	}
	return visitedNodes
}

func (d *Day) Day10(input []string) {
	var startingPipe *Pipe
	startI := 0
	startJ := 0
	pipesMap := make([][]*Pipe, len(input))

	// Create the pipes
	for i, line := range input {
		pipesMap[i] = make([]*Pipe, len(line))
		for j, char := range line {
			if string(char) == "." {
				continue
			}
			pipesMap[i][j] = &Pipe{
				pattern: string(char),
				x:       i,
				y:       j,
			}
		}
	}

	// Link the pipes
	for i, line := range input {
		for j, char := range line {
			switch string(char) {
			case "|":
				pipesMap[i][j].north = pipeAtCoordinatesOrNil(pipesMap, i-1, j, "|", "7", "F")
				pipesMap[i][j].south = pipeAtCoordinatesOrNil(pipesMap, i+1, j, "|", "L", "J")
			case "-":
				pipesMap[i][j].east = pipeAtCoordinatesOrNil(pipesMap, i, j+1, "-", "J", "7")
				pipesMap[i][j].west = pipeAtCoordinatesOrNil(pipesMap, i, j-1, "-", "L", "F")
			case "L":
				pipesMap[i][j].north = pipeAtCoordinatesOrNil(pipesMap, i-1, j, "|", "7", "F")
				pipesMap[i][j].east = pipeAtCoordinatesOrNil(pipesMap, i, j+1, "-", "J", "7")
			case "J":
				pipesMap[i][j].north = pipeAtCoordinatesOrNil(pipesMap, i-1, j, "|", "7", "F")
				pipesMap[i][j].west = pipeAtCoordinatesOrNil(pipesMap, i, j-1, "-", "L", "F")
			case "7":
				pipesMap[i][j].south = pipeAtCoordinatesOrNil(pipesMap, i+1, j, "|", "L", "J")
				pipesMap[i][j].west = pipeAtCoordinatesOrNil(pipesMap, i, j-1, "-", "L", "F")
			case "F":
				pipesMap[i][j].south = pipeAtCoordinatesOrNil(pipesMap, i+1, j, "|", "L", "J")
				pipesMap[i][j].east = pipeAtCoordinatesOrNil(pipesMap, i, j+1, "-", "J", "7")
			case "S":
				startingPipe = pipesMap[i][j]
				startI = i
				startJ = j
			}
		}
	}

	// Link the starting pipe
	if pipeAtCoordinatesOrNil(pipesMap, startI, startJ+1, "-", "J", "7") != nil {
		startingPipe.east = pipesMap[startI][startJ+1]
		pipesMap[startI][startJ+1].west = startingPipe
	}
	if pipeAtCoordinatesOrNil(pipesMap, startI, startJ-1, "-", "L", "F") != nil {
		startingPipe.west = pipesMap[startI][startJ-1]
		pipesMap[startI][startJ-1].east = startingPipe
	}
	if pipeAtCoordinatesOrNil(pipesMap, startI+1, startJ, "|", "L", "J") != nil {
		startingPipe.south = pipesMap[startI+1][startJ]
		pipesMap[startI+1][startJ].north = startingPipe
	}
	if pipeAtCoordinatesOrNil(pipesMap, startI-1, startJ, "|", "7", "F") != nil {
		startingPipe.north = pipesMap[startI-1][startJ]
		pipesMap[startI-1][startJ].south = startingPipe
	}

	mainLoop := deepestNodesList(startingPipe)
	fmt.Println("Part 1:", len(mainLoop)/2)

	// Solution taken and adapted from https://github.com/ianmihura/advent23/blob/master/day_10/day_10.go#L88
	// Shoelace formula (make sure the loop is counter-clockwise)
	trailing := 0
	for i := range mainLoop {
		if i == len(mainLoop)-1 {
			trailing += (mainLoop[i].y + mainLoop[0].y) * (mainLoop[i].x - mainLoop[0].x)
		} else {
			trailing += (mainLoop[i].y + mainLoop[i+1].y) * (mainLoop[i].x - mainLoop[i+1].x)
		}
	}
	area := trailing / 2

	// Pick's theorem
	inner := area - (len(mainLoop) / 2) + 1

	fmt.Println("Part 2:", inner)
}
