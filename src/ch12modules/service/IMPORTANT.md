# IMPORTANT: Service Example Compilation

## This is a STANDALONE multi-module example

The modules in this directory (`tour.api`, `tour.hiking`, `tour.wine`, `tour.app`) are **self-contained** and meant to be compiled **separately** from the main JavaOCP17 project.

## Why You're Seeing Errors

Your IDE is trying to compile these modules as part of the main project, which causes conflicts because:
- The main project has no module structure (or a different module structure)
- These are independent modules with their own module-info.java files
- The IDE gets confused by multiple module descriptors

## Solutions

### Option 1: Exclude from IDE (Recommended)

**IntelliJ IDEA:**
1. Right-click on the `service` directory
2. Select "Mark Directory as" → "Excluded"
3. This tells the IDE to ignore this directory when compiling the main project

**Eclipse:**
1. Right-click on the `service` directory
2. Select "Build Path" → "Exclude"

**VS Code:**
Add to `.vscode/settings.json`:
```json
{
  "java.project.sourcePaths": [
    {"path": "src", "exclude": "ch12modules/service/**"}
  ]
}
```

### Option 2: Compile Separately

These modules are meant to be compiled from within the `service` directory:

```bash
cd src/ch12modules/service

# Compile all modules
javac -d out --module-source-path . \
    tour.api/module-info.java tour.api/com/tour/api/*.java \
    tour.hiking/module-info.java tour.hiking/com/tour/hiking/*.java \
    tour.wine/module-info.java tour.wine/com/tour/wine/*.java \
    tour.app/module-info.java tour.app/com/tour/app/*.java

# Run the application
java --module-path out --module tour.app/com.tour.app.TourApp
```

See `README.md` for complete instructions.

## This is Normal

This is a **reference example** showing how real multi-module projects work. It's intentionally separate from the main study notes project structure.
