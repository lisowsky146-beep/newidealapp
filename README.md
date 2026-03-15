# VinylCollectionApp

Android app for managing a vinyl collection with:
- local Room database
- add/search/edit/delete records
- barcode scanner (CameraX + ML Kit)
- OCR from a selected cover photo (ML Kit Text Recognition)
- Discogs release search
- collection valuation based on saved estimated values
- GitHub Actions release APK build

## Required secret for Discogs
Create a repository secret named `DISCOGS_TOKEN` in GitHub Actions settings.
The app still builds without it, but Discogs search will return an error until the token is added.

## Build on GitHub
The workflow is in `.github/workflows/android.yml`.
Open **Actions** and run **Android CI** or push a commit to `main`.
