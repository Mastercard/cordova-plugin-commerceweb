# Contributing

## Forking

This repository must be forked before any code can be merged. Pushing 
directly to the upstream repo is not allowed. 

## Commits

Commits should be atomic. They should succinctly encapsulate any implementation
changes. Do not include unrelated code changes from mulitple classes across the 
project on the same commit.

### Logs

If your commit addresses a reported bug, append your commit with `#<issue_number>`


## Pull Requests

PRs to the upstream repo will be reviewed as soon as possible. If they are 
approved, they will be merged into the upstream repo and synced with all 
downstream forks.


## Code Quality

Code submitted within a Pull Request will run against the Jenkins build pipeline.
As part of this build, the following metrics are captured:

* Code coverage %
* Analysis of lint warnings

Please ensure you are resolving all lint warning prior to opening a PR.


 