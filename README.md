# AD Challenge

# todo
* [+] define architecture
* [+] setup plumbing
* [+] build project skeleton
* [+] setup test infrastructure
* [+] define deployment/running strategy
* [-] add business logic
* [-] refactor
* [-] document


# Architecture

Phases to achieve certain results and control scope   

## [+] Phase I
One app (Distribution)
* Interface: REST backed by Play Framework (Scala)
* Persistence in memory
* Business Logic: Hello world page via Play

## [+] Phase II
One app (Distribution + OAuth)
* OAuth provider for Play

## [+] Phase III
One app (Distribution + OAuth + Users)
* New Apis

## Phase IV
* persistence backed by embedded cassandra

## Phase V
Four apps: Distribution, OAuth, Users, Internal Backend

## Phase VI
Dockeralize the apps



# Quick Start

### Configure application
The default OAuth settings could be found in `conf/ad.conf`

`
ad {
  OAuthConsumerKey = ad-challenge-product-i-XXXXXX
  OAuthConsumerSecret = XXXXXXX
}
`

### Start application

*Pre-requisites:* JDK version 8 

To start an application from the command line
`bin/activator run`

Check application is running properly by opening url in browser: [http://localhost:9000](http://localhost:9000)

### Start ngrok
`bin/ngrok http 9000`

Copy url like `https://e94ea19a.ngrok.io` and enter in AD product page



# Licensing

This program is licensed under the Apache License, version 2. See the LICENSE file for details.