# Maxwell's Demon
## COSC 150 - Advanced Programming

- [Goals](#goals)
- [Description](#description)
- [Specification and requirements](#specification-and-requirements)
- [Provided files](#provided-files)
- [Grading rubric](#grading-rubric)

## Goals

In order to complete this assignment, I was required to:
- Understand how to design a graphical user interface
  - Design the look of a graphical interface
  - Implement a design using Swing containers and components
- Understand how to model a simple physical system
  - Choose appropriate data model and storage strategy for a game
  - Understand how to use the model to create a program view
- Understand how to use event-driven programming to update the game
  - Choose appropriate event generators like buttons or timers
  - Create corresponding event listeners to respond to these events
  - Properly update the model in response to events

## Description

The 19th century mathematical physicist, James Maxwell, is best-known for developing a set of four equations (known as Maxwell's equations) that describe classical electormagnetism. He also made contributions to statistical thermodynamics (producing the Maxwell-Boltzmann distribution for ideal gas behavior); during this work, he conceived of a thought experiment in which the second law of thermodynamics could be violated.

> ... if we conceive of a being whose faculties are so sharpened that he can follow every molecule in its course, such as a being, whose attributes are as essentially finite as our own, would be able to do what is impossible to us. For we have seen that molecules in a vessel full of air at uniform temperature are moving with velocities by no means uniform, though the mean velocity of any great number of them, arbitrarily selected, is almost exactly uniform. Now let us suppose that such a vessel is divided into two portions, A and B, by a division in which there is a small hole, and that a being, who can see the individual molecules, opens and closes this hole, so as to allow only the swifter molecules to pass from A to B, and only the slower molecules to pass from B to A. He will thus, without expenditure of work, raise the temperature of B and lower that of A, in contradicition to the second law of thermodynamics.
- Bennet, Charles H. (November 1987). "Demons, Engines, and the Second Law" (PDF). Scientific American. 257(5): 108-116

In other words, Maxwell envisioned a box split into two halves, with a door between the parts that could be opened or closed by a "demon" who observed every particle in the box. By carefully opening and closing the door at the correct moments, the demon could place all the fast-moving (i.e., hot) particles on one side of the door and all the slow-moving (i.e., cold) particles on the other side, reducing the entropy of the system.

## Specification and requirements

### Specification

In this assignment I created a Java program that lets the user play as Maxwell's demon. I created a GUI for the program, an appropriate data model to store the state of the game, and controllers to allow the game to update over time and in response to the user.

#### The Maxwell's Demon Interface

The game will start by displaying a user interface with the following components:
- A wide panel "playing area" divided in half by a line/wall down the middle (to create two chambers).
  - A collection of color-coded particles (red for hot, fast-moving particles and blue for cold, slow-moving particles).
  - A mouse-responsive "door" in the wall between the two chambers.
    - When the user clicks and holds ("presses") the mouse button, the door should open so particles can move between the two chambers.
    - When the user releases the mouse button, the door should close.
- A button which, when clicked, creates one red particle and one blue particle on each side of the play area (adding four total particles to the game).
- A button which, when clicked, resets the game to its starting state (as if the game had just begun).
- A visual display of the "temperature" of each of the two chambers.

#### Tracking Individual Particles

Throughout the execution of your game, I keep track of all the particles currently "present" in the game, and understand how to draw them on screen and update their positions over time. This requires tracking
- The position and velocity of each particle
- The temperature/color of the particle (to draw it)

For the purpose of this assignment, 
- "fast" denotes particles moving between 4 and 6 centimeters per second
- "slow" denotes speeds between 2 and 4 centimeters per second
 
(This is more manageable than the more realistic 200 meters per second that the median particle is moving in your room right now.) These values will permit transitions to screens with different resolutions with reasonable behavior.

#### Computing the Average Temperature

This project also requried computing/tracking the average temperature of all the particles on each side of the game. The temperature is proportional to the average of the squares of the speed of the particles on that side of the game.
