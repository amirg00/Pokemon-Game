# Object-Oriented Programming Exercise 4


<!-- Graphical user -->
### Graphical user interface


<p align="center">
<img align="center" src="https://s10.gifyu.com/images/Untitled13.gif"/>
</p>

---
(For zoom in click on the image).

In this project we were asked to display the graph visually,

we chose to represent the graph using Java Swing with a panel that allows uploading a graph using a JSON file.

Saving a graph to a JSON file, adding and deleting a vertex, adding and deleting an Edge,

Access to see the whole process of the algorithms in real time (Shorted path, isConnected, Travelling Salesman Problem (TSP), Center).

In addition, we added a help button that links directly to Git.

----------------

<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary>Content</summary>
  <ol>
    <li><a href="#graphical-user-interface">Graphical user interface</a></li>
    <li><a href="#about-the-project">About The Project</a></li>
    <li><a href="#game-details">Game Details</a></li>
    <li><a href="#code-details">Code Details</a></li>
    <li><a href="#algorithms">Algorithms</a></li>
    <li><a href="#performance-analysis">Performance Analysis</a></li>
    <li><a href="#how-to-run">How  to run</a></li>
    <li><a href="#languages-and-tools">Languages and Tools</a></li>
    <li><a href="#acknowledgements">Acknowledgements</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>

----------------

<!-- ABOUT THE PROJECT -->
# About The Project
**_Object-Oriented Programming Exercise 4 - Directed Weighted Graph:_**

This document presents the final (last) assignment for the OOP course (CS.Ariel 2021),
In this assignment, you will be asked to “put into practice” the main tools covered along the course, in particular, you are expected to design a “Pokemon game” in which given a weighted graph,  a set of “Agents” should be located on it so they could “catch” as many “Pokemons” as possible. The pokemons are located on the graph’s (directed) edges, therefore, the agent needs to take (aka walk)  the proper edge to “grab” the pokemon (see below for more info). Your goal is to maximize the overall sum of weights of the “grabbed” pokemons (while not exceeding the maximum amount of server calls allowed in a second - 10 max)

"The origins of graph theory are humble, even frivolous :round_pushpin:"

---------

<!-- code-details -->

## Game Details

<table>
    <thead>
        <tr>
            <th>Player</th>
            <th>Info</th>
            <th>Details</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td rowspan=4>
            <center><b>Ash Ketchum</b></center>
  <img height="130" width="120"  src="https://s10.gifyu.com/images/output-onlinegiftools81393cf074f6111f.gif" alt="Flowers" style="width:auto;">
</td>
            <td>Speed</td>
            <td>from 1 to 5</td>
        </tr>
        <tr>
            <td>Number of Points</td>
            <td> the amount of points is calculated by the values of the Pokemons that the agent caught.</td>
        </tr>
        <tr>
            <td>Location</td>
            <td>the current location of the agent in the arena.</td>
        </tr>
        <tr>
            <td>Next Move</td>
            <td>the next act of the agent.</td>
        </tr>
    </tbody>
</table>

|Name:Bulbasaur, Value: 5|Name:Charmander, Value: 5|Name:Squirtle, Value: 5|
|:---: |:---: |:---: |
|<img src="https://s10.gifyu.com/images/dbn0v3y-5a4a15ed-3aab-4095-853f-44ff3363c5bd0b615df73cb2a928.gif" alt="dbn0v3y-5a4a15ed-3aab-4095-853f-44ff3363c5bd0b615df73cb2a928.gif" border="0">|<img src="https://s10.gifyu.com/images/dbpdu7d-5c767e8b-0e1d-4be6-bd3b-24d3666b455c.gif" alt="dbpdu7d-5c767e8b-0e1d-4be6-bd3b-24d3666b455c.gif" border="0">|<img src="https://s10.gifyu.com/images/dbps0np-bfaf045a-5781-40cf-a5e8-3f9c45e5b45bcf0eeb931dfafbd3.gif" alt="dbps0np-bfaf045a-5781-40cf-a5e8-3f9c45e5b45bcf0eeb931dfafbd3.gif" border="0">

|Name:Ivysaur, Value: 6-10|Name:Charmeleon, Value: 6-10|Name:Wartortle, Value: 6-10|
|:---: |:---: |:---: |
|<img src="https://s10.gifyu.com/images/dbp9pdd-b495eb25-8a62-48f0-a2e5-18bf730b30f7.gif" border="0">|<img src="https://s10.gifyu.com/images/dbpdw4y-652b62bb-3f48-493a-bae7-f5d9011b6156.gif" alt="dbpdu7d-5c767e8b-0e1d-4be6-bd3b-24d3666b455c.gif" border="0">|<img src="https://s10.gifyu.com/images/dbn31pn-c4c27c2c-99aa-404b-b675-b3cfd5abe9c0.gif" alt="dbps0np-bfaf045a-5781-40cf-a5e8-3f9c45e5b45bcf0eeb931dfafbd3.gif" border="0">


|Name:Venusaur, Value: 11-15|Name:Charizard, Value: 11-15|Name:Blastoise, Value: 11-15|
|:---: |:---: |:---: |
|<img src="https://s10.gifyu.com/images/dbp9pls-ba6e2a01-b9b9-4b93-98ce-cf318c48df3c.gif" border="0">|<img src="https://s10.gifyu.com/images/dbn13p6-033f0445-1d59-45df-90a9-673eae27cbb9-1.gif" alt="dbpdu7d-5c767e8b-0e1d-4be6-bd3b-24d3666b455c.gif" border="0">|<img src="https://s10.gifyu.com/images/dbn31r0-071c53d5-eff7-4311-8bcb-f77214d0a404.gif" alt="dbps0np-bfaf045a-5781-40cf-a5e8-3f9c45e5b45bcf0eeb931dfafbd3.gif" border="0">

---------

<!-- code-details -->

## Code Details


Unified Modeling Language (UML) :

Click the image for zoom in.
<p align="center">
<img align="center" src="https://s10.gifyu.com/images/UML33297de35ecdd2d8.png" />
</p>

As you can see in UML the Client class were given by the creator were asked to implement the student code with include the connecting to the server run it.

The Agent Class we Created all the information about the Agent such as id, src , dest posistion, value and speed the agent got and Geolocation with the positon of the Agent, in addition we create a function that isonway that check if this Agent is available and if the Agent is close to Pokemon.

The Pokémon class include all the information that Pokémon needs such as geolocation position and current edge value and tpye, we crated update edge function that take the pokemon and update with the positon on the edges.

The StageController include the all process such as init Pokémon, move Agents that control on the move agents choosenextedge that calculate with our algorithm the best move.

We have GUI package that include all gui that represent visually the graph and the game of the pokemon that means the agent running to catch the pokemons to get the higher score.

in addition, we are used the algorithms and the graph from the previous exercises   


---------
<!-- algorithms -->
## Algorithms

In this project we used a number of algorithms, we will present the algorithms that were implemented in this project.

[Dijkstra's algorithm](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm) to find the shortest path between a and b.
It picks the unvisited vertex with the lowest distance, calculates the distance through it to each unvisited neighbor, and updates the neighbor's distance if smaller. Mark visited (set to red) when done with neighbors.


---------


<!-- Performance -->

## Performance Analysis

**These analyze were tested on a computer with an Intel i7 processor and 16 GB of RAM WIN10**

|Stage No.|Grade |Moves|
|:---: |:---: |:---:|
|0|a|112.92|
|1|a|48.92|
|2|a|30.49|
|3|b|511.187|
|4|c|490.155|
|5|d|507.468|
|6|a|20.82|
|7|b|184.483|
|8|c|174.600|
|9|d|184.911|
|10|a|26.15|
|11|b|35.228|
|12|c|38.524|
|13|d|39.093|
|14|d|39.093|
|15|d|39.093|

---------
<!-- how-to-run -->
# How to run

_Jar file:_

* <big>**<u>_THE JAR MUST BE IN THE PROJECT FOLDER!!_</u>**</big>

* _Jar  file name:  Ex4_Server_v0.0.jar_

Run Jar file  in commend line:

```
java -jar Ex4_Server_v0.0.jar <stage number>
```


In this project we used some external libraries in the JAVA language, in order to make life easier these libraries are located within the project called external libraries.

First, it's important to make sure you clone this project in IntelliJ through Terminal.
To be sure:
```
git clone https://github.com/amirg00/Pokemon-Game.git
```

Second, in this project we used some external libraries in the JAVA language, in order to make life easier these libraries are located within the project called external libraries.
In order to update these libraries in this project, we will do the following:
```
File -> Project Structure -> Libraries and select the folder with all external libraries.
```

_**Java SDK Verison:**_ ```15```<br>
_**Project language level:**_ ```15 - Text blocks```

_External libraries:_
* _gson-2.8.2_
* _javax.json-1.1.4_
* _javax.ws.rs-api-2.1.1_
* _json-simple-1.1.1_
* _json-2.3.1_

---------


## Languages and Tools

  <div align="center">

<code><img height="50" width="50" src="https://icon-library.com/images/java-icon-png/java-icon-png-15.jpg"></code>
<code><img height="40" width="70" src="https://upload.wikimedia.org/wikipedia/commons/d/d5/UML_logo.svg"/></code>
<code><img height="40" width="40" src="https://upload.wikimedia.org/wikipedia/commons/thumb/9/9c/IntelliJ_IDEA_Icon.svg/768px-IntelliJ_IDEA_Icon.svg.png"/></code>
<code><img height="40" height="40" src="https://raw.githubusercontent.com/github/explore/80688e429a7d4ef2fca1e82350fe8e3517d3494d/topics/git/git.png"></code>
<code><img height="40" height="40" src="https://raw.githubusercontent.com/github/explore/80688e429a7d4ef2fca1e82350fe8e3517d3494d/topics/terminal/terminal.png"></code>
  </div>


<!-- ACKNOWLEDGEMENTS -->
## Acknowledgements
* [Java](www.java.com)
* [UML](https://en.wikipedia.org/wiki/Unified_Modeling_Language)
* [Git](https://git-scm.com/)
* [IntelliJ](https://www.jetbrains.com/)
* [Git-scm](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)


<!-- CONTACT -->
## Contact <small>[Top▲](#graphical-user-interface)</small>


Gal - [here](https://github.com/GalKoaz/)

Amir - [here](https://github.com/amirg00/)

Project Link: [here](https://github.com/GalKoaz/OOP-Ex2)

___

Copyright © _This Project was created on Jan. 07, 2021, by [Gal](https://github.com/GalKoaz/)  & [Amir](https://github.com/amirg00/)_.
