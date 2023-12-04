package main

import (
	"bufio"
	"log"
	"os"
	"reflect"
)

func openFile(filename string) []string {
	file, err := os.Open(filename)
	if err != nil {
		log.Fatal(err)
	}
	defer file.Close()

	var lines []string
	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		lines = append(lines, scanner.Text())
	}

	return lines
}

type T struct{}

func main() {
	t := T{}

	// get the latest `DayX` method
	tType := reflect.TypeOf(&t)
	var latestMethod string
	for i := 0; i < tType.NumMethod(); i++ {
		method := tType.Method(i).Name
		if latestMethod == "" || method > latestMethod {
			latestMethod = method
		}
	}

	// manually set the latest method to run
	// latestMethod = "Day1"

	// run the latest `DayX` method
	dayDigits := latestMethod[3:]
	reflect.ValueOf(&t).MethodByName(latestMethod).Call([]reflect.Value{
		reflect.ValueOf(openFile("inputs/input" + dayDigits + ".txt")),
	})
}
