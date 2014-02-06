#!/bin/bash
gradle build
cp build/libs/*.war $CIRCLE_ARTIFACTS
scp build/libs/*.war deploy@dev.grubdice.co:grubdice.war
ssh deploy@dev.grubdice.co "/home/deploy/deploy.sh"
