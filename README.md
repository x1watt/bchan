# bchan

[![Build APK](https://github.com/x1watt/bchan/actions/workflows/build_release.yml/badge.svg)](https://github.com/x1watt/bchan/actions/workflows/build_release.yml)
[![Latest release](https://img.shields.io/github/v/release/x1watt/bchan?label=download&color=E5353B)](https://github.com/x1watt/bchan/releases/latest)
[![License: Apache-2.0](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](./LICENSE)

**bchan** is a free and open-source manga & comic reader for Android 8.0+. It is a
streamlined fork of [TachiyomiSY](https://github.com/jobobby04/TachiyomiSY) (itself
based on [Mihon](https://github.com/mihonapp/mihon)), tuned to work out of the box
with sensible defaults so you can start reading with as little setup as possible.

> bchan installs as its own app (`app.bchan`) — it does not interfere with, read,
> or migrate data from any existing Tachiyomi/Mihon install.

## Download

Grab the latest APK from the **[Releases page](https://github.com/x1watt/bchan/releases/latest)**.

If you are unsure which file to pick, download **`bchan.apk`** (the universal build).
Per-architecture builds (`arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64`) are smaller and
provided for advanced users.

To install, enable *Install unknown apps* for your browser/file manager, then open the
downloaded APK.

## What makes bchan different

bchan keeps the full feature set of Mihon/TachiyomiSY and adds a more "it just works"
experience on top:

- **Extensions ready out of the box** — the [Keiyoushi](https://keiyoushi.github.io/)
  extension repository is seeded on first run, and the extension-trust gate is removed,
  so you can install sources immediately.
- **Webtoon reading by default** — continuous vertical scroll is the default reading
  mode, which is what most modern content expects.
- **Resilient downloads** — downloads automatically pause and resume across network
  changes instead of failing, and survive switching between Wi-Fi and mobile data.
- **Flat, predictable download naming** — downloaded chapters use a clean, consistent
  on-disk layout.
- **Quality-of-life touches** — extension-update counts surfaced on the Extensions tab,
  global search defaulting to *All*, persistent favorite covers, and more.

## Core features (inherited from Mihon / TachiyomiSY)

- Online reading from a wide range of sources via installable extensions
- Local reading of downloaded content
- A configurable reader with multiple viewers, reading directions, and settings
- Library categories, filtering, and search
- Library update scheduling for new chapters
- Tracker support: MyAnimeList, AniList, Kitsu, MangaUpdates, Shikimori, Bangumi
- Light and dark themes with dynamic theming
- Local and cloud backups
- Source migration, manga info editing, recommendations, and the many extras from the SY fork

## Building from source

bchan is a standard Gradle Android project. You need JDK 17.

```bash
# Debug build (auto-signed, installable)
./gradlew assembleDevDebug

# Optimized release build (unsigned)
./gradlew assembleDevRelease
```

The resulting APKs are written to `app/build/outputs/apk/`.

CI builds (see [`.github/workflows/build_release.yml`](.github/workflows/build_release.yml))
produce signed release APKs and publish them to the Releases page automatically when a
`v*` tag is pushed, or on a manual workflow run.

## Contributing

See [CONTRIBUTING.md](./CONTRIBUTING.md) and the
[Code of Conduct](./CODE_OF_CONDUCT.md).

## License & credits

bchan is licensed under the [Apache License 2.0](./LICENSE).

It builds on the excellent work of the [Mihon](https://github.com/mihonapp/mihon) and
[TachiyomiSY](https://github.com/jobobby04/TachiyomiSY) projects and their many
contributors. bchan is an independent fork and is not affiliated with or endorsed by
those projects.

bchan does not host, provide, or bundle any content. Sources are added by the user
through third-party extensions, and the user is responsible for the content they access.
