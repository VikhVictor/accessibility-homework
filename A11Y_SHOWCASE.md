# Intentional accessibility defects

This repository is a teaching showcase. Accessibility defects in both application
modules are intentional and must not be fixed as incidental cleanup.

- `:app` is the original Android Views implementation.
- `:app-compose` reproduces the same screens, behavior, and TalkBack problems in
  Jetpack Compose.
- `:data` is the shared repository layer.

## Views-to-Compose defect map

| Teaching case | Android Views | Jetpack Compose |
| --- | --- | --- |
| Unlabelled navigation | Back and cart images have no `contentDescription` | Back and cart `Image` nodes use `contentDescription = null` |
| Undersized targets | Back and cart icons are clickable at 20dp | Automatic target expansion is disabled and both controls remain 20dp |
| Silent cart updates | The badge is plain text with no announcement | The badge has no live-region semantics |
| Decorative treatment of useful images | Product, empty-cart, discount, address, payment, toggle, and arrow images are unlabelled | Equivalent images intentionally have no descriptions |
| Fake buttons | Add-to-cart and checkout controls are clickable `TextView`s | They are clickable `Text` nodes without `Role.Button` |
| Missing document structure | Section labels are plain `TextView`s | Section labels have no heading semantics |
| Fragmented selectors | Address/payment text and checkbox are separate focus targets | Text and `Checkbox` remain separate; the checkbox has no accessible label |
| Tiny delivery selector | Only the unlabelled 20dp toggle image changes the option | Only the unlabelled 20dp toggle changes the option; no selected-state semantics are exposed on the row |
| No result feedback | Checkout success/error callbacks are commented out | Checkout confirmation intentionally produces no visible or announced result |
| Carousel navigation | Vertical lists contain horizontal lists | The same nested horizontal carousels are used |

The Compose instrumentation tests assert representative defects. A failing test may
mean somebody accidentally improved accessibility. In this project that requires an
explicit teaching-task decision, not a drive-by fix.

## Verification

```shell
./gradlew :app:assembleDebug :app-compose:assembleDebug
./gradlew :app-compose:testDebugUnitTest
./gradlew :app-compose:connectedDebugAndroidTest
```
