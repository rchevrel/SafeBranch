# SAFE BRANCH
## A Git visualizer with release note generation

@author : Régis CHEVREL - rchevrel.net

## Big Picture


```mermaid
---
title: "Git extraction to diagram and release note production"
---
stateDiagram-v2
    direction LR
    state SafeBranch {
        state SafeBranchRepository {
            readGit : Read Git with Jgit
            wrapGit : JGit wrapper for Safe Branch
        }
        note left of SafeBranchRepository : "My note"
        state SafeBranchGraph {
            direction TB
            computeGitTree : a git compute
            %% createDiagram
            note right of computeGitTree : "My note"
        }
        state SafeBranchRenderer {
            renderDiagram
            note right of renderDiagram : Expected target formats\n- Markdown Mermaid (Diagram As Text)\n- Markdown Mermaid as pako URL\n- Draw.io XML
        }
        state SafeBranchReleaseNoteGenerator {
            produceReleaseNote
            note right of produceReleaseNote : "My not"
        }
    }
    [*] --> readGit
    readGit --> wrapGit
    wrapGit --> computeGitTree
    %% computeGitTree --> createDiagram
    computeGitTree --> produceReleaseNote
    computeGitTree --> renderDiagram
```
> *diagram generated with Safe Brach (rchevrel.net/SafeBranch)*
```mermaid
stateDiagram-v2
    %%direction LR
    state SafeBranch {
        direction LR
        SafeBranchRepository
        SafeBranchGraph
        SafeBranchReleaseNoteGenarator
        SafeBranchRenderer
    }
    %% note right of SafeBranch : Une bonne note qui fait plaisir\nà lire et qui est un  peu longe\n et bien descriptive
    [*] --> JGit : Connect to remote Git repository
    %% note left of JGit : Une bonne note qui fait plaisir\nà lire et qui est un  peu longe\n et bien descriptive
    JGit --> SafeBranchRepository : > org.eclipse.jgit.api.Git
    SafeBranchRepository --> SafeBranchGraph : Explore Git and create branch precedence order
    SafeBranchGraph --> SafeBranchReleaseNoteGenarator
    SafeBranchGraph --> SafeBranchRenderer
    
```

```mermaid
   stateDiagram
   direction TB

   accTitle: This is the accessible title
   accDescr: This is an accessible description

   classDef notMoving fill:white
   classDef movement font-style:italic
   classDef badBadEvent fill:#f00,color:white,font-weight:bold,stroke-width:2px,stroke:yellow

   [*]--> Still
   Still --> [*]
   Still --> Moving
   Moving --> Still
   Moving --> Crash
   Crash --> [*]

   class Still notMoving
   class Moving, Crash movement
   class Crash badBadEvent
   class end badBadEvent

```

<details>

<summary>Diagram</summary>

```mermaid
---
title: Simple sample
---
stateDiagram-v2
    direction LR
    Still
    2001
    note right of 2001 : This is the note.
    [*] --> John
    [*] --> 2001
    state John {
        Hello
        Bye
        [*] --> Hello
        [*] --> Hello
        [*] --> Bye : other1
    }
    [*] --> Still
    Still --> [*]
    Still --> Moving
    Moving --> Still
    Moving --> Crash
    Crash --> [*]
```

</details>

## Architecture

```mermaid
---
title: Animal example
---
classDiagram
    note "From Duck till Zebra"
    Animal <|-- Duck
    note for Duck "can fly\ncan swim\ncan dive\ncan help in debugging"
    Animal <|-- Fish
    Animal <|-- Zebra
    Animal : +int age
    Animal : +String gender
    Animal: +isMammal()
    Animal: +mate()
    class Duck{
        +String beakColor
        +swim()
        +quack()
    }
    class Fish{
        -int sizeInFeet
        -canEat()
    }
    class Zebra{
        +bool is_wild
        +run()
    }

```

## Tests
