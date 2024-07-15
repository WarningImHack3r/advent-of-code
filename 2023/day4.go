package main

import (
	"fmt"
	"slices"
	"strconv"
	"strings"
)

type Card struct {
	id             int
	winningNumbers []int
	yourNumbers    []int
}

func checkForWin(card Card) int {
	count := 0
	for _, n := range card.yourNumbers {
		if slices.Contains(card.winningNumbers[:], n) {
			count++
		}
	}
	return count
}

func (d *Day) Day4(input []string) {
	winsSum := 0
	cards := make([]Card, 0)

	for _, line := range input {
		initialSplit := strings.Split(line, ": ")
		cardId := strings.Fields(initialSplit[0])[1]
		cardIdInt, err := strconv.Atoi(cardId)
		if err != nil {
			fmt.Printf("Error converting card id (%v) to int: %v\n", cardId, err)
		}

		grids := strings.Split(initialSplit[1], " | ")
		winningNumbers := make([]int, 0)
		yourNumbers := make([]int, 0)

		// Populate lists
		for _, n := range strings.Fields(grids[0]) {
			var num int
			num, err = strconv.Atoi(n)
			if err != nil {
				fmt.Printf("Error converting winning number (%v) to int: %v\n", n, err)
			}
			winningNumbers = append(winningNumbers, num)
		}

		for _, n := range strings.Fields(grids[1]) {
			var num int
			num, err = strconv.Atoi(n)
			if err != nil {
				fmt.Printf("Error converting your number (%v) to int: %v\n", n, err)
			}
			yourNumbers = append(yourNumbers, num)
		}

		cards = append(
			cards, Card{
				id:             cardIdInt - 1,
				winningNumbers: winningNumbers,
				yourNumbers:    yourNumbers,
			},
		)
	}

	// Part 1: Increase the score on each win
	for _, card := range cards {
		score := 0
		for i := 0; i < checkForWin(card); i++ {
			if score == 0 {
				score = 1
			} else {
				score *= 2
			}
		}
		winsSum += score
	}

	// Part 2: Add the n next cards again to the list if n wins
	cardsCounts := make([]int, len(cards))
	for i := 0; i < len(cards); i++ {
		cardsCounts[i] = 1
	}
	cardsCopy := make([]Card, len(cards))
	copy(cardsCopy, cards)
	for len(cardsCopy) > 0 {
		newCards := make([]Card, 0)
		for _, card := range cardsCopy {
			for i := 0; i < checkForWin(card); i++ {
				cardsCounts[card.id+i+1]++
				newCards = append(newCards, cards[card.id+i+1])
			}
		}
		cardsCopy = newCards
	}
	countsSum := 0
	for _, count := range cardsCounts {
		countsSum += count
	}

	fmt.Printf("Part 1: %d\n", winsSum)
	fmt.Printf("Part 2: %d\n", countsSum)
}
