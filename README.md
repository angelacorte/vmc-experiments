# _An Aggregate Vascular Morphogenesis Controller for Engineered Self-Organising Spatial Structures_

#### Authors temporarily anonymised for double-blind review

### Table of Contents
- [About](#about)
    * [Experiments](#experiments)
- [Getting Started](#getting-started)
  - [Requirements](#requirements)
  - [Reproduce the entire experiment](#reproduce-the-entire-experiment)
    * [Reproduce the experiments through Gradle](#reproduce-the-experiments-through-gradle)
    * [Simulations Graphical Interface](#simulations-graphical-interface)

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

In order to successfully download and execute the experiments, [Git](https://git-scm.com), [Java](https://www.oracle.com/java/technologies/javase/jdk19-archive-downloads.html) 19 (or higher)
and [Gradle](https://gradle.org) 8.7 are needed. 

### Reproduce the entire experiment

**WARNING**: re-running the whole experiment may take a very long time on a normal computer.

#### Reproduce the experiments through Gradle

1. Install a Gradle-compatible version of Java. 
Use the [Gradle/Java compatibility matrix](https://docs.gradle.org/current/userguide/compatibility.html) to learn which is the compatible version range. 
The Version of Gradle used in this experiment can be found in the gradle-wrapper.properties file located in the gradle/wrapper folder.

2. Clone this repository on your pc with `git clone git@github.com:angelacorte/vmc-experiments.git` (ssh).

3. Move into the root folder and run the following command:

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

#### Simulations Graphical Interface

The simulation environment and graphical interface are provided by [Alchemist Simulator](https://alchemistsimulator.github.io/index.html).
To understand how to interact with the GUI,
please refer to the [Alchemist documentation](https://alchemistsimulator.github.io/reference/swing/index.html#shortcuts).