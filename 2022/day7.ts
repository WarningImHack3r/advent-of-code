import { readFile } from "fs";

const day = __filename.split("/").pop()?.match("\\d+")?.[0];
readFile(__dirname + `/inputs/input${day}.txt`, (err, data) => {
    if (err) {
        console.error(err);
        return;
    }
    const input = data.toString().split("\n").filter(x => x.length > 0);

    let pwd: string[] = [];
    const dirsChilds: Record<string, string[]> = {};
    const dirsSizes: Record<string, number> = {};
    // Loop through each line of input
    input.forEach(line => {
        // Determine if the line is a command or an output
        if (line.startsWith("$ ")) {
            // Command
            const command = line.substring(2);
            if (command.startsWith("cd ")) {
                // Change directory
                const destDir = command.split(" ")[1];
                if (destDir === "..") {
                    pwd.pop();
                } else if (destDir === "/") {
                    pwd = [];
                } else {
                    // Real directory
                    pwd.push(destDir);
                }
                const location = "/" + pwd.join("/");
                dirsChilds[location] = dirsChilds[location] || [];
                dirsSizes[location] = dirsSizes[location] || 0;
                console.log("[$] Changed directory to", destDir, "with childs", dirsChilds[location], "and size", dirsSizes[location]);
            }
            // The only other command is "ls", which is not needed
        } else {
            // Output of a ls command (only possible command output)
            const location = "/" + pwd.join("/");
            if (line.startsWith("dir ")) {
                dirsChilds[location].push(line.substring(4));
                console.log("[->] Added child", line.substring(4), "to", location);
            } else {
                dirsSizes[location] += parseInt(line.substring(0, line.indexOf(" ")));
                console.log("[->] Added size", line.substring(0, line.indexOf(" ")), "to", location);
            }
        }
    });
    // Append size of childs to size of parent recursively
    Object.keys(dirsChilds).forEach(dir => dirsSizes[dir] += sizeOfSubdirectories(dir, dirsChilds, dirsSizes));

    // Display result
    console.log("[o] Total size of dirs <= 100_000:", Object.values(dirsSizes).filter(x => x <= 100_000).reduce((a, b) => a + b)); // part 1
    const freeSpace = 70_000_000 - dirsSizes["/"];
    const minDeleteNeeded = 30_000_000 - freeSpace;
    console.log("[o] Biggest dir to delete to free space:", Object.values(dirsSizes).sort((a, b) => a - b).filter(x => x >= minDeleteNeeded)[0]); // part 2
});

function sizeOfSubdirectories(directory: string, dirsChilds: Record<string, string[]>, dirsSizes: Record<string, number>, recursive = false): number {
    console.log("[c] Calculating size of", directory, `(${dirsSizes[directory]}) with childs`, dirsChilds[directory], recursive ? "(recursive)" : "");
    const path = directory + (directory === "/" ? "" : "/");
    return dirsChilds[directory].map(child => dirsSizes[path + child] + sizeOfSubdirectories(path + child, dirsChilds, dirsSizes, true)).reduce((a, b) => a + b, 0);
}
