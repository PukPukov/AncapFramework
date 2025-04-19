set shell := ["nu", "-c"]

shoot:
    mvnd package -pl Artifex -am -Dmaven.test.skip=true -Dskip.enforcer=true

release:
    mvnd install -pl Artifex -am

reload:
    mvn clean -T 1C
    mvnd --stop