# ssh-circle

Creates a ssh build from the latest [CircleCI](https://circleci.com) build in a project and launches a terminal to ssh in.

## Installation

[Download the latest uberjar](https://raw.github.com/dwwoelfel/ssh-circle/artifacts/artifacts/ssh-circle-standalone.jar) created in a CircleCI deployment step.


## Usage

First, get a CircleCI API token from [your account page](https://circleci.com/account/api).

To start an ssh build for this project:

    CIRCLE_TOKEN=:circle-token java -jar ssh-circle-standalone dwwoelfel/ssh-circle

## License

Copyright © 2013

Distributed under the Eclipse Public License, the same as Clojure.
