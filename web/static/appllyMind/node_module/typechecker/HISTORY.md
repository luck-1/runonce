# History

## v4.5.0 2018 February 17
- asynchronous functions are now detected as functions, before there was no support for them
    - `getType(async function () {})` now returns `function`
    - `isFunction(async function () {})` returns `true`
    - `isAsyncFunction(async function () {})` returns `true`
    - `isSyncFunction(async function () {})` returns `false`
    - Thanks to [David Kebler](https://github.com/dkebler) for [issue #17](https://github.com/bevry/typechecker/issues/17)

## v4.4.1 2017 January 18
- Made custom type map argument in `getType` actually work
    - Thanks to [David Kebler](https://github.com/dkebler) for [issue #15](https://github.com/bevry/typechecker/issues/15)

## v4.4.0 2016 November 11
- Use `require` over `import`
- More efficient `isObject` check
    - Fixes `isObject` and `isPlainObject` not returning booleans in some cases
        - Thanks to [athlordJojo](https://github.com/athlordJojo) for [issue #12](https://github.com/bevry/typechecker/issues/12)
- More comprehensive native vs conventional class tests
- Updated base files
    - Removes docs being contained in npm package
        - Thanks to [Adam Demasi](https://github.com/kirb) for [issue #14](https://github.com/bevry/typechecker/issues/14)
- Updated dependencies

## v4.3.0 2016 March 21
- Removed `getTypes` as it was ambiguous, internal and no one external used it
- Exposed `typeMap` which links types to the methods that check for them, used by `getType`

## v4.2.1 2016 March 20
- Fixed packing issue

## v4.2.0 2016 March 20
- Now defined as individual methods
- Repackaged

## v4.1.0 2016 January 15
- Now defined as a class with static methods instead of an object with functions as that is essentially what it is
- Repackaged

## v4.0.1 2015 December 9
- Updated base files
- Updated dependencies

## v4.0.0 2015 September 21
- Added new `map` and `weakmap` types that the `getType` method can now return
- Added the methods:
    - `isMap` (checks for Map instance)
    - `isWeakMap` (checks for WeakMap instance)

## v3.0.0 2015 August 27
- Added new `class` type that the `getType` method can now return
- Added the methods:
    - `isClass` (checks for native and conventional classes)
    - `isNativeClass` (checks for native ES6 classes)
    - `isConventionalClass` (checks for functions that start with a capital letter)
- Anonymous compiled/non-native classes may be detected as functions instead of as classes. If you rely on class detection, be aware of this, and document this to your users accordingly.

## v2.1.0 2015 August 26
- Fixed `isEmpty` - it use to return the opposite of what was empty
- Converted from CoffeeScript to ES6+
- Updated base files
- Everything is now tested thoroughly

## v2.0.8 2013 November 1
- Dropped component.io and bower support, just use ender or browserify

## v2.0.7 2013 October 27
- Re-packaged

## v2.0.6 2013 September 18
- Fixed node release (since v2.0.5)
- Fixed bower release (since v2.0.4)

## v2.0.5 2013 September 18
- Fixed node release (since v2.0.4)

## v2.0.4 2013 September 18
- Fixed cyclic dependency problem on windows (since v2.0.3)
- Added bower support

## v2.0.3 2013 September 18
- Attempt at fixing circular dependency infinite loop (since v2.0.2)

## v2.0.2 2013 September 18
- Added component.io support

## v2.0.1 2013 March 27
- Fixed some package.json properties

## v2.0.0 2013 March 27
- Split typeChecker from [bal-util](https://github.com/balupton/bal-util)
