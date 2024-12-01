package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"reflect"
	"regexp"
	"strconv"
	"strings"
)

func readFile(filename string) []string {
	file, err := os.Open(filename)
	if err != nil {
		log.Fatal(err)
	}
	defer func(file *os.File) {
		err = file.Close()
		if err != nil {
			log.Fatal(err)
		}
	}(file)

	var lines []string
	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		lines = append(lines, scanner.Text())
	}

	return lines
}

type Day struct {
	part1, part2 *int
}

func (d *Day) SetPart1Answer(answer int) {
	d.part1 = &answer
}

func (d *Day) SetPart2Answer(answer int) {
	d.part2 = &answer
}

var numberRegex = regexp.MustCompile(`\d+`)

const (
	reset = "\033[0m"
	blue  = "\033[34m"
	cyan  = "\033[36m"
)

func main() {
	var d Day

	// get the latest `DayX` method
	dType := reflect.TypeOf(&d)
	var latestMethod string
	var latestDay int
	for i := 0; i < dType.NumMethod(); i++ {
		method := dType.Method(i).Name
		if !strings.HasPrefix(method, "Day") {
			continue
		}
		dayDigits := numberRegex.FindString(method)
		day, err := strconv.Atoi(dayDigits)
		if err != nil {
			log.Fatal(err)
		}
		if latestMethod == "" || day > latestDay {
			latestMethod = method
			latestDay = day
		}
	}

	// manually set the method to run
	// latestMethod = "Day1"
	// latestDay = 1

	// run the latest `DayX` method
	reflect.ValueOf(&d).MethodByName(latestMethod).Call(
		[]reflect.Value{
			reflect.ValueOf(readFile("inputs/input" + strconv.Itoa(latestDay) + ".txt")),
		},
	)

	// print the results
	if d.part1 != nil || d.part2 != nil {
		fmt.Println()
	}
	if d.part1 != nil {
		fmt.Printf("%s[Day %d]%s Part 1:%s %v\n", blue, latestDay, cyan, reset, *d.part1)
	}
	if d.part2 != nil {
		fmt.Printf("%s[Day %d]%s Part 2:%s %v\n", blue, latestDay, cyan, reset, *d.part2)
	}
}
