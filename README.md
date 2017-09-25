## Cargenie Challenge Solution by Liangjun Jiang

## Instruction
To compile & run, 

step 1:
```
javac -cp httpclient-4.5.3.jar:commons-logging-1.2.jar:httpcore-4.4.6.jar:commons-io-2.5.jar Main.java
```

Step 2:
```
java -cp httpclient-4.5.3.jar:commons-logging-1.2.jar:httpcore-4.4.6.jar:commons-io-2.5.jar: Main http://dist.pravala.com/coding/multiGet-example.zip /Users/l.jiang/Desktop/result.zip
```

Sample result
```
bytes= 0-1049000
bytes= 1049001-2098000
bytes= 2098001-3147000
bytes= 3147001-4196000
Download finished
```

## Implementation v.s. Requirement
1. Source URL should be specified with a required command-line option
 *the first parameter is for source URL. the 2nd parameter is for saved path. If 2nd parameter is not provide, the downloaded file will be saved to the root directory of source code (or jar file)*
2 .File is downloaded in 4 chunks (4 requests made to the server)
* Yes. A for-loop with total count of 4 is executed.
3. Only the first 4 MiB of the file should be downloaded from the server
* Yes. 1 Mib = 1,049,000 bytes. The downloaded file has a size of 4,096,000 bytes* 
4. Output file may be specified with a command-line option (but may have a default if not set)
*Yes. explained in 1.
5. No corruption in file - correct order, correct size, correct bytes
(you don’t need to verify correctness, but it should not introduce errors)
*Used `http://dist.pravala.com/coding/multiGet-example.zip` as a target file, and verified its correctness.
6. File is retrieved with GET requests
*Yes. GET request is used.*

## Further Thoughts
1. Didn't really get into the chance to provide an interface to have the three optional features. Even though 2 and 3 of the options have been configured in the source code provided.
2. Run `multiGet-darwin-386` under MacOS gives me an error with source url `http://dist.pravala.com/coding/multiGet-example.zip`.





