package main

import (
	"fmt"
	"sort"
)

type Pair struct {
	hand string
	bid int
}

type Rank uint8
const (
	HighCard Rank = iota
	OnePair
	TwoPairs
	ThreeOfAKind
	FullHouse
	FourOfAKind
	FiveOfAKind
)

func rankForHand(hand string) Rank {
	occurrences := make(map[rune]int)
	for _, card := range hand {
		occurrences[card]++
	}
	// Sort the values of the map
	values := make([]int, 0, len(occurrences))
	for card, count := range occurrences {
		if card == 'J' { // Part 2 - ignore J counts to add them later to the first value
			continue
		}
		values = append(values, count)
	}
	sort.Slice(values, func(i, j int) bool {
		return values[i] > values[j]
	})
	fmt.Println(hand, values)

	// Part 2 - add the occurrences of J to the first value
	if len(values) > 0 {
		values[0] += occurrences['J']
	} else {
		values = append(values, occurrences['J'])
	}

	// Check for five of a kind
	for _, count := range values {
		if count == 5 {
			return FiveOfAKind
		}
		if count == 4 {
			return FourOfAKind
		}
		if count == 3 {
			if len(values) == 2 {
				return FullHouse
			}
			return ThreeOfAKind
		}
		if count == 2 {
			if len(values) == 3 {
				return TwoPairs
			}
			return OnePair
		}
	}

	return HighCard
}

func strongestHand(hand1, hand2 string) string {
	rank1 := rankForHand(hand1)
	rank2 := rankForHand(hand2)

	if rank1 > rank2 {
		return hand1
	}
	if rank2 > rank1 {
		return hand2
	}

	// Same rank, compare the first different card
	// const cards = "AKQJT98765432" // Part 1
	const cards = "AKQT98765432J" // Part 2 - J is now the weakest card in 1-1 comparison
	for i := 0; i < len(hand1); i++ {
		if hand1[i] != hand2[i] {
			// Find the index of the card in the cards string
			for j := 0; j < len(cards); j++ {
				if hand1[i] == cards[j] {
					return hand1
				}
				if hand2[i] == cards[j] {
					return hand2
				}
			}
		}
	}

	return hand1
}

func (d *Day) Day7(input []string) {
	pairs := make([]Pair, len(input))

	// Parse input
	for i, line := range input {
		var hand string
		var bid int
		fmt.Sscanf(line, "%s %d", &hand, &bid)
		pairs[i] = Pair{hand, bid}
	}

	sort.Slice(pairs, func(i, j int) bool {
		return strongestHand(pairs[i].hand, pairs[j].hand) == pairs[j].hand
	})

	totalWinnings := 0
	for i, pair := range pairs {
		totalWinnings += pair.bid * (i+1)
		fmt.Printf("%d: %s %d\n", i+1, pair.hand, pair.bid)
	}

	fmt.Println("Total winnings:", totalWinnings)
}
