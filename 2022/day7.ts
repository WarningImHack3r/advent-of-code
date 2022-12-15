import { readFile } from "fs";

readFile(__dirname + "/inputs/input7.txt", (err, data) => {
    if (err) {
        console.error(err);
        return;
    }
    const input = data.toString().split("\n").filter(x => x.length > 0);

    let parent = "";
    let location = "";
    let depth = 0;
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
                    depth--;
                    console.log("[$] Parent of", location, "is", parent);
                    location = parent;
                } else {
                    depth = destDir === "/" ? 0 : depth + 1;

                    let prefix = depth > 0 ? depth.toString() : "";
                    const parents = Object.keys(dirsChilds).filter(x => dirsChilds[x].includes(location));
                    if (parents.length > 1 && !dirsChilds[parent].includes(location)) {
                        prefix += "-" + (parents.indexOf(parent) + 1);
                        console.log("[$] Found multiple parents", parents, "for location", location, "-> new prefix", prefix);
                    }
                    prefix += "_";

                    if (destDir !== "/") {
                        parent = location;
                    }
                    location = prefix + destDir;
                }
                dirsChilds[location] = dirsChilds[location] || [];
                dirsSizes[location] = dirsSizes[location] || 0;
                console.log("[$] Changed directory to", location, "with childs", dirsChilds[location], "and size", dirsSizes[location]);
            }
        } else {
            // Output of a ls command
            if (line.startsWith("dir ")) {
                dirsChilds[location].push((depth + 1).toString() + "_" + line.substring(4));
                console.log("[->] Added child", (depth + 1).toString() + "_" + line.substring(4), "to", location);
            } else {
                dirsSizes[location] += parseInt(line.substring(0, line.indexOf(" ")));
                console.log("[->] Added size", line.substring(0, line.indexOf(" ")), "to", location);
            }
        }
    });
    Object.keys(dirsChilds).forEach(dir => {
        // console.log("[+] Starting for", dir, "with size", dirsSizes[dir]);
        const size = sizeOfSubdirectories(dir, dirsChilds, dirsSizes);
        dirsSizes[dir] += size;
        // console.log("[*] New size for", dir, "with size", dirsSizes[dir]);
    });

    console.log("[o] Result:", Object.values(dirsSizes).filter(x => x <= 100_000).reduce((a, b) => a + b));
});

function sizeOfSubdirectories(directory: string, dirsChilds: Record<string, string[]>, dirsSizes: Record<string, number>): number {
    // console.log("[o] Calculating size of", directory, `(${dirsSizes[directory]}) with childs`, dirsChilds[directory]);
    return dirsChilds[directory].map(child => dirsSizes[child] + sizeOfSubdirectories(child, dirsChilds, dirsSizes)).reduce((a, b) => a + b, 0);
}
