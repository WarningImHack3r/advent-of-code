package main

import (
	"bufio"
	"log"
	"os"
	"reflect"
	"regexp"
)

func openFile(filename string) []string {
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

func main() {
	d := Day{}

	// get the latest `DayX` method
	dType := reflect.TypeOf(&d)
	var latestMethod string
	for i := 0; i < dType.NumMethod(); i++ {
		method := dType.Method(i).Name
		if latestMethod == "" || method > latestMethod {
			latestMethod = method
		}
	}

	// manually set the latest method to run
	// latestMethod = "Day1"

	// run the latest `DayX` method
	dayDigits := regexp.MustCompile(`\d+`).FindString(latestMethod)
	reflect.ValueOf(&d).MethodByName(latestMethod).Call(
		[]reflect.Value{
			reflect.ValueOf(openFile("inputs/input" + dayDigits + ".txt")),
		},
	)
}
