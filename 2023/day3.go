package main

import (
	"strconv"
	"unicode"
)

type Item struct {
	content         string
	isSymbol        bool
	xStart          int
	xEnd            int
	y               int
	adjacentSymbols []Item // Only for symbols in part 2, contains adjacent numbers
}

func (d *Day) Day3(input []string) {
	var currentItem *Item
	items := make([]Item, 0)
	partNumbers := make([]Item, 0)

	// Objectify numbers and symbols
	for j, line := range input {
		for i, char := range line {
			if char == '.' {
				if currentItem != nil {
					currentItem.xEnd = i - 1
					items = append(items, *currentItem)
					currentItem = nil
				}
				continue
			} else {
				isSymbol := !unicode.IsNumber(char)
				if currentItem == nil {
					currentItem = &Item{
						content:  string(char),
						isSymbol: isSymbol,
						xStart:   i,
						y:        j,
					}
				} else {
					if currentItem.isSymbol != isSymbol {
						currentItem.xEnd = i - 1
						items = append(items, *currentItem)
						currentItem = &Item{
							content:  string(char),
							isSymbol: isSymbol,
							xStart:   i,
							y:        j,
						}
					} else {
						currentItem.content += string(char)
					}
				}
			}
		}
	}

	// Save neighbor numbers for each symbol
	for i, symbol := range items {
		if symbol.isSymbol {
			for _, number := range items {
				if !number.isSymbol {
					if number.y == symbol.y {
						if number.xStart == symbol.xEnd+1 || number.xEnd == symbol.xStart-1 {
							// Adjacent same line
							partNumbers = append(partNumbers, number)
							items[i].adjacentSymbols = append(items[i].adjacentSymbols, number)
						}
					} else if number.y == symbol.y+1 || number.y == symbol.y-1 {
						// Adjacent upper or lower line
						if number.xStart == symbol.xStart || number.xStart == symbol.xStart+1 || number.xEnd == symbol.xEnd || number.xEnd == symbol.xEnd-1 || (number.xStart < symbol.xStart && number.xEnd > symbol.xEnd) {
							partNumbers = append(partNumbers, number)
							items[i].adjacentSymbols = append(items[i].adjacentSymbols, number)
						}
					}
				}
			}
		}
	}

	// Part 1: Sum all part numbers
	sum := 0
	for _, number := range partNumbers {
		num, err := strconv.Atoi(number.content)
		if err != nil {
			panic(err)
		}
		sum += num
	}
	d.SetPart1Answer(sum)

	// Part 2: Sum all ratios of 2 numbers around a '*' symbol
	ratiosSum := 0
	for _, symbol := range items {
		if symbol.isSymbol && symbol.content == "*" && len(symbol.adjacentSymbols) == 2 {
			gearRatio := 1
			for _, number := range symbol.adjacentSymbols {
				num, err := strconv.Atoi(number.content)
				if err != nil {
					panic(err)
				}
				gearRatio *= num
			}
			ratiosSum += gearRatio
		}
	}
	d.SetPart2Answer(ratiosSum)
}
