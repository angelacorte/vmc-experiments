# _An Aggregate Vascular Morphogenesis Controller for Engineered Self-Organising Spatial Structures_

### Authors 

- **Angela Cortecchia** (*) -- angela.cortecchia@unibo.it
- **Danilo Pianini** (*) -- danilo.pianini@unibo.it
- **Giovanni Ciatto** (*) -- giovanni.ciatto@unibo.it
- **Roberto Casadei** (*) -- roby.casadei@unibo.it 

(*) *Department of Computer Science and Engineering | Alma Mater Studiorum -- Università di Bologna | Cesena, Italy*

### Table of Contents
- [About](#about)
    * [Experiments](#experiments)
- [Getting Started](#getting-started)
  - [Requirements](#requirements)
  - [Reproduce the entire experiment](#reproduce-the-entire-experiment)
    * [Reproduce the experiments through Gradle](#reproduce-the-experiments-through-gradle)
    * [Simulation Graphical Interface](#simulation-graphical-interface)

## About

In the field of evolutionary computing, the concept of Vascular Morphogenesis Controller (VMC) 
has been proposed in to model the growth of artificial structures over time.

A thorough analysis of the VMC model revealed some limitations:
- assumes the organization structure is a tree, here intended as a directed acyclic graph with a single root and with a single path connecting the root with each leaf;
- the model is implicitly synchronous, as it assumes that (i) the evaluation of the nodes must proceed from the leaves to the root (and back), and (ii) the update of the whole tree occurs atomically.
  
Although, depending on the context, these assumptions may be acceptable, in general they may induce (possibly hidden) 
abstraction gaps when VMC is used to model real-world systems, and, at the same time, limit the applicability of the 
pattern to engineered morphogenetic systems.

To address these limitations, in this work, we propose *FieldVMC*: a generalisation of the VMC model as a field-based 
computation, in the spirit of the Aggregate Programming (AP) paradigm.

### Experiments

This repository contains the source code for the experiments presented in the paper
"_An Aggregate Vascular Morphogenesis Controller for Engineered Self-Organising Spatial Structures_".

The experiments want to show the capabilities of the proposed model in generating self-organising spatial structures.

Some examples of the generated structures are shown below:

|   ![starting_structure](./images/cutting01.png)    |        ![self-organised_structure](./images/cutting19.png)        |
|:--------------------------------------------------:|:-----------------------------------------------------------------:|
|                *Starting Structure*                |                    *Self-Organised Structure*                     |
| ![structure_after_cutting](./images/cutting21.png) | ![self-organised_structure_after_cutting](./images/cutting27.png) |
|       *Structure after cutting a part of it*       |           *Self-Organised Structure after the cutting*            | 

The images show the evolution of a structure from a starting configuration to a self-organised structure.

The goal of this evaluation is to show that the proposed FieldVMC supports the construction of the same structures of its 
predecessor, and, in addition, that it can work in scenarios not previously investigated. 
To this end, we designed a set of five experiments:
- self-construction from a single node (growth from seed),
- self-repair after disruption (network segmentation) with no regeneration (cutting),
- self-integration of multiple FieldVMC systems (grafting)
- self-segmentation of a larger structure (budding), and
- self-optimisation of multiple large structures into a more efficient one (abscission and regrowth).

## Getting started

### Requirements

In order to successfully download and execute the experiments are needed: 
- Internet connection;
- [Git](https://git-scm.com);
- Linux, macOS and Windows systems capable of running [Java](https://www.oracle.com/java/technologies/javase/jdk19-archive-downloads.html) 17 (or higher);
- 1GB free space on disk;
- GPU with minimal OpenGL capabilities (OpenGL 2.0);
- 4GB RAM.

### Limitations

- The experiments do not generate any form of data to be evaluated on, the evaluation is purely visible at the moment;
- On different monitor types with different resolutions, the graphical interface could appear a bit different;
- For GUI interpretation, please refer to [Simulation Graphical Interface](#simulation-graphical-interface) section.

### Reproduce the entire experiment

**WARNING**: re-running the whole experiment may take a very long time on a normal computer.

#### How to read the experiments

In all the experiments, the cyan area represents the resource and the yellow area the success, with darker shades indicating higher values.

Nodes are represented as circles.
The root is identified by a dark outer circumference.

The size of a circle depends on the amount of resource and success received
relative to all other nodes in the system:
we fix the maximum possible size `D`,
we compute the maximum amount of resource `R`
and the maximum amount of success $S$
across all nodes in the system;
then, for each node in the system with success $s$ and resource $r$,
we determine its size $d$ proportionally to $D$ as ``` \displaystyle d=\frac{D (r + s)}{R + S} ```

Their color depends on the amount of resource nodes have
and is assigned based on the hue of the HSV color space,
with the most resource associated with indigo,
and the lowest with red.

Dashed lines are communication channels, solid black lines represent the tree structure, and green (resp. orange) lines depict
the resource (resp. success) distribution flows, the thicker they are, the more resource (resp. success) is being transferred.

#### Extremely quick-start of a basic experiment -- `(ba|z|fi)?sh` users only

- Requires a Unix terminal (`(ba|z|fi)?sh`)
- `curl` must be installed
- run 
``` 
curl https://raw.githubusercontent.com/angelacorte/vmc-experiments/master/vmc-basic-example.sh | bash 
``` 
- the repository is in your `Downloads` folder for further inspection.

#### Reproduce the experiments through Gradle

1. Install a Gradle-compatible version of Java. 
Use the [Gradle/Java compatibility matrix](https://docs.gradle.org/current/userguide/compatibility.html) to learn which is the compatible version range. 
The Version of Gradle used in this experiment can be found in the gradle-wrapper.properties file located in the gradle/wrapper folder.

2. Open a terminal

3. Clone this repository on your pc with `git clone git@github.com:angelacorte/vmc-experiments.git` (ssh).

4. Move into the root folder and run the following command:

```shell
./gradlew run<ExperimentName>Graphic
```
substituting `<ExperimentName>` with the name of the experiment (in PascalCase) specified in the YAML simulation file.

Or execute ```./gradlew tasks``` to view the list of available tasks.

The corresponding YAML simulation files to the experiments cited above are the following:
- _oneRoot_: self-construction from a single node (growth from seed) `./gradlew runOneRootGraphic`,
- _cutting_: self-repair after disruption (network segmentation) with no regeneration (cutting) `./gradlew runCuttingGraphic`, 
- _graft_: self-integration of multiple FieldVMC systems (grafting) `./gradlew runGraftGraphic`,
- _graftWithMoreLeaders_: self-segmentation of a larger structure (budding) `./gradlew runGraftWithMoreLeadersGraphic`, and
- _graftWithSpawning_: self-optimisation of multiple large structures into a more efficient one (abscission and regrowth) `./gradlew runGraftWithSpawningGraphic`.

**NOTE:**
The tasks above will run the experiments with the default parameters, that are the one on which the evaluation has been performed.

#### Changing experiment's parameters


#### Simulation Graphical Interface

The simulation environment and graphical interface are provided by [Alchemist Simulator](https://alchemistsimulator.github.io/index.html).
To understand how to interact with the GUI,
please refer to the [Alchemist documentation](https://alchemistsimulator.github.io/reference/swing/index.html#shortcuts).