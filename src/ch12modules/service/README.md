# Java Module Services Example

A complete, working example of the Java Module Service pattern.

## Project Structure

```
service/
├── tour.api/                          <- SPI MODULE (Service Provider Interface)
│   ├── module-info.java               <- exports + uses
│   └── com/tour/api/
│       ├── Tour.java                  <- SPI (interface)
│       ├── TourDetails.java           <- Referenced class
│       └── TourFinder.java            <- Service Locator
│
├── tour.hiking/                       <- PROVIDER MODULE
│   ├── module-info.java               <- requires + provides...with
│   └── com/tour/hiking/
│       └── HikingTour.java            <- Implementation
│
├── tour.wine/                         <- PROVIDER MODULE
│   ├── module-info.java               <- requires + provides...with
│   └── com/tour/wine/
│       └── WineTour.java              <- Implementation
│
└── tour.app/                          <- CONSUMER MODULE
    ├── module-info.java               <- requires (only!)
    └── com/tour/app/
        └── TourApp.java               <- Consumer
```

## Module Directives Summary

| Module       | exports         | requires    | uses           | provides...with              |
|--------------|-----------------|-------------|----------------|------------------------------|
| tour.api     | com.tour.api    | -           | com.tour.api.Tour | -                         |
| tour.hiking  | -               | tour.api    | -              | Tour with HikingTour         |
| tour.wine    | -               | tour.api    | -              | Tour with WineTour           |
| tour.app     | -               | tour.api    | -              | -                            |

## Compile and Run Instructions

### From the `service` directory:

```bash
# Navigate to service directory
cd src/ch12modules/service

# Step 1: Compile all modules
javac -d out --module-source-path . \
    tour.api/module-info.java tour.api/com/tour/api/*.java \
    tour.hiking/module-info.java tour.hiking/com/tour/hiking/*.java \
    tour.wine/module-info.java tour.wine/com/tour/wine/*.java \
    tour.app/module-info.java tour.app/com/tour/app/*.java

# Step 2: Run the application
java --module-path out --module tour.app/com.tour.app.TourApp
```

### Creating Module JARs (optional):

```bash
# Create mods directory
mkdir mods

# Create JARs for each module
jar --create --file mods/tour.api.jar -C out/tour.api .
jar --create --file mods/tour.hiking.jar -C out/tour.hiking .
jar --create --file mods/tour.wine.jar -C out/tour.wine .
jar --create --file mods/tour.app.jar --main-class com.tour.app.TourApp -C out/tour.app .

# Run from JARs
java -p mods -m tour.app
```

## Expected Output

```
=== Tour Application (Service Consumer) ===

Available Tours:
--------------------------------------------------

Tour: Mountain Hiking Adventure
Price: $149.99
Description: Experience breathtaking mountain views on this guided hiking tour
Duration: 6 hours
Max Group Size: 12
Difficulty: Moderate

Tour: Vineyard Wine Tasting Tour
Price: $89.99
Description: Visit three local vineyards and taste award-winning wines
Duration: 4 hours
Max Group Size: 20
Difficulty: Easy

--------------------------------------------------
Total tours found: 2

=== Using ServiceLoader.stream() ===

Available Tour implementations:
  - com.tour.hiking.HikingTour
  - com.tour.wine.WineTour
```

## Key Exam Points

1. **Service = SPI + Referenced Classes + Service Locator** (NOT implementations!)

2. **Service Locator needs `uses` directive** - without it, ServiceLoader returns empty

3. **Providers need `provides...with`** - this is how ServiceLoader finds them

4. **Consumer only needs `requires`** - no `uses`, no requires on providers

5. **`stream()` returns `Provider` objects** - must call `get()` to get instance

6. **Implementations don't need to be exported** - `provides` gives ServiceLoader access
