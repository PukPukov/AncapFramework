Artifex is runtime part of AncapFramework.

Splitting the code into a plugin installed by the user and libraries included in the final plugin is intended to solve the following problems:
— Optimize final plugin weights by splitting common code into common jar
— Achieve interop between plugins and interop-based optimizations

The problem of this approach is need to maintain absolute backward compatibility. This leads to a significant slowdown in development when new features cannot be added due to the presence of legacy code. A rash decision would be to both ensure that compatibility will be preserved forever, also as break it with every release, creating hell for users of the framework (which is what is happening now in modding for Minecraft). But, fortunately, we can compromise and combine the advantages of the two approaches — don't add everything to common code, let plugin developers shade non-interop-intended parts of framework and break backward compatibility there when needed, but preserve compatibility in shared code. That's the difference of 1.7 approach and pre-1.7 approach — previously everything was in Artifex, but now all non-interop logic is supposed to be shaded.