# AI Framework Stage v0.1

This directory contains the independent foundation for the future Mob cognition
and behavior systems. It does not replace vanilla Mob AI.

## Completed

- Perception layer integration points
- `MobMind` cognitive state layer
- `SuspicionMemory`
- `PerceptionMemory`
- `BehaviorController` execution layer
- `MobAIAdapter` entity capability abstraction
- The initial `TURNING` behavior module

## Not implemented

- Runtime Mob integration
- `DecisionSystem`
- Investigation behavior
- Search behavior
- Alert propagation
- Combat execution, including attacking and chasing

The current separation is:

```text
Perception
    -> MobMind
    -> BehaviorController
    -> MobAIAdapter
```

`MobMind` maintains cognitive state and provides inputs for a future decision
layer. It does not choose or execute movement, pathfinding, attacks,
investigations, searches, or target selection. `BehaviorController` executes
behavior phases through the adapter, while the adapter exposes only the
capabilities needed by those phases.

`AlertState.COMBAT` means that a hostile target identity has been confirmed and
the mob remains in a persistent hostile cognitive state. It does not mean that
the mob is currently attacking, chasing, moving, or executing any combat
action. Those concerns belong to future behavior modules.
