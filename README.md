<a id="readme-top"></a>
<br />
<div align="center">

<h3 align="center">AutoScript</h3>
<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
## About The Project

![image](https://github.com/user-attachments/assets/c7574641-447a-4e03-ae0c-07713dc12a63)

This repository contains the code for AutoScript, a simple programming language developed 
during the Automata Theory course at FUAS by Tsvetislav Rangelov and Aleksandra Krasteva.

AutoScript is a strongly-typed, multi-paradigm programming language.Sharing lexical similarities with Typescript,
it supports a lot of the generic procedures available in other languages, such as conditional statements,
loops, logical operators, numeric arithmetic and typed functions. 

Additionally, it has its own set of differentiating characteristics. Some examples include:
1. No use of 'any' types for variables or functions.
2. Only arrow functions (Similar to ES6).
3. Only types variables.
4. Typed function parameters.
5. Mandatory return types on functions.

AutoScript also supports very primitive scoping, in the sense that the scoping depth only goes down to 1 level.
This in turn means that we only have a local and global scope at any given point of program execution.
Scoping is implemented via nested symbol tables, where the root symbol table represents the global scope, and
any (or in this case just one) nested symbol tables represent local scopes (for function bodies, loop bodies etc.).

Some other features include type checks during variable reassignment, type safe arrays, and array indexing.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Built With

Java 18 and Antlr.
<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started
The following sections explain how to get a local build of the grammar and java binary.

### Prerequisites
You need a JDK >= v18 installed on your machine. Additionally, Antlr is necessary to build the grammar. 
A jar file containing the Antlr binaries is present in the folder Research that you can directly use without downloading Antlr yourself.

Once you have those dependencies installed, you can compile and run the project with an demo input that reverses an array of numbers.
Simply run the `build-and-run.sh` present within Research.
* npm
  ```sh
  cd Research/
  chmod +x build-and-run.sh
  ./build-and-run.sh
  ```
<!-- CONTACT -->
## Contact

t.rangelov@student.fontys.nl
a.krasteva@student.fontys.nl

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<p align="right">(<a href="#readme-top">back to top</a>)</p>
