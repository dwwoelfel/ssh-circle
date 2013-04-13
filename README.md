# ssh-circle

Launches a terminal that SSHs into a retry of the latest [CircleCI](https://circleci.com) build for a given project.

## Installation

[Download the latest uberjar](https://raw.github.com/dwwoelfel/ssh-circle/artifacts/artifacts/ssh-circle-standalone.jar) (created in a CircleCI deployment step!).


## Usage

First, get a CircleCI API token from [your account page](https://circleci.com/account/api).

To start an ssh build for this project:

    CIRCLE_TOKEN=:circle-token java -jar ssh-circle-standalone.jar dwwoelfel/ssh-circle

## Bugs

Terminal launching throws exceptions on non-macs :(

## License

Copyright © 2013

Distributed under the Eclipse Public License, the same as Clojure.
