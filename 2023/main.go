package main

import (
	"bufio"
	"log"
	"os"
	"reflect"
	"regexp"
	"strconv"
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

type Day struct{}

var numberRegex = regexp.MustCompile(`\d+`)

func main() {
	var d Day

	// get the latest `DayX` method
	dType := reflect.TypeOf(&d)
	var latestMethod string
	var latestDay int
	for i := 0; i < dType.NumMethod(); i++ {
		method := dType.Method(i).Name
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
}
