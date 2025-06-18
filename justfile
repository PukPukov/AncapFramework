set shell := ["nu", "-c"]

shoot:
    jh21 mvnd package -pl Artifex -am -Dmaven.test.skip=true -Dskip.enforcer=true

release:
    jh21 mvnd install -pl Artifex -am

reload:
    jh21 mvn clean -T 1C
    jh21 mvnd --stop