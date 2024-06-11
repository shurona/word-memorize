module.exports = {
    apps: [{
        name : "word-memory-server",
        script: Java,
        args : [
            "-jar",
            "../../build/libs/wordfinder-0.0.1-SNAPSHOT.jar"
        ],
        exec_mode: cluster,
        wait_ready: true,
    }]
}