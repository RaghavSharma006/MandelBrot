# Mandelbrot & Julia Set Explorer

Explore complex numbers through fractals: an original **Java Swing desktop application**, a live **Mandelbrot Atlas** study website, and eight mathematical reference PDFs.

**[Open Mandelbrot Atlas](https://fractal-space-explorer.vercel.app/)** · **[Mandelbrot](https://fractal-space-explorer.vercel.app/mandelbrot)** · **[Julia](https://fractal-space-explorer.vercel.app/julia)** · **[Learn & proofs](https://fractal-space-explorer.vercel.app/learn)**

![Mandelbrot set rendered by the Java application](https://github.com/user-attachments/assets/baa4ccc8-797d-47cd-b664-7d0065606654)

## Choose how to explore

| | Mandelbrot Atlas — web | Java Swing — desktop |
| --- | --- | --- |
| Start | [Open in your browser](https://fractal-space-explorer.vercel.app/) | Compile and run the Java sources below |
| Calculation | Java backend; the browser displays PNGs | Java computes and displays the image locally |
| Explore | Zoom, pan, inspect coordinates, trace orbits and place a Julia marker | Zoom, pan, read coordinates and generate a Julia window |
| Study | Explained proofs, diagrams and a downloadable handbook | Eight source PDFs in `Proofs/` |
| Save | Capture a PNG with separate detail settings | Save the current image to the Desktop folder |

This repository contains the original Swing implementation and reference PDFs. The live website is a separate deployment; its React frontend and HTTP adapter are not part of this repository's current `master` tree.

## Run the desktop application

You need a **JDK 11 or newer**, including `javac`, and a graphical desktop. The application uses standard Java Swing, AWT and ImageIO; no external libraries or build tool are required.

```bash
git clone https://github.com/RaghavSharma006/MandelBrot.git
cd MandelBrot
javac -d out src/Main.java src/MandelBrot.java src/Julia.java
java -cp out Main
```

Alternatively, open the project in IntelliJ IDEA, select a JDK and run `src/Main.java`.

The Swing renderer uses the system display dimensions. Higher screen resolution and iteration limits increase render time. A graphical session is required; this application is not a headless command-line renderer.

## Desktop controls

| Control | Action |
| --- | --- |
| Single left-click in Mandelbrot | Read the clicked point's real and imaginary coordinates |
| Double left-click | Zoom in around the clicked point |
| Double right-click | Zoom out around the clicked point |
| Drag | Pan across the complex plane |
| Zoom field | Set the multiplier used by the next zoom; the default is `1.25` |
| Iterations field | Set the maximum steps for the next render; the desktop default is `5000` |
| Real / Imaginary fields + Generate | Open a Julia window for the chosen constant `c` |
| Save | Write a PNG to your user's `Desktop` directory |

Julia windows have their own zoom and iteration controls. To change the save location, edit `desktopPath` in `saveImageToFolder()` in `MandelBrot.java` and `Julia.java`. If saving fails, check that the destination directory exists and inspect the console message.

## Web controls and study workflow

1. Open the [Mandelbrot explorer](https://fractal-space-explorer.vercel.app/mandelbrot). Drag to pan; scroll or double-left-click to zoom in. Double-right-click zooms out.
2. Open **Inspect** to enter a complex coordinate, center the Mandelbrot view or trace its orbit. Place a marker to choose a point directly on the image.
3. Choose **Create Julia set** to use that coordinate as the fixed constant `c`, or enter a constant in the [Julia explorer](https://fractal-space-explorer.vercel.app/julia).
4. Use **Fullscreen** for the viewer. With the viewer focused, arrow keys pan, `+` / `−` zoom and `Home` resets. `Esc` exits fullscreen.
5. Explore with the preview settings, then use **Capture** for a detailed PNG. High-iteration captures can take minutes.

Web defaults are **200 Mandelbrot iterations** and **1,000 Julia iterations**. Capture supports up to **100,000 iterations**. These are web settings, separate from the Swing defaults. Resolution controls pixel sampling; iterations control how long each pixel's orbit is tested.

## The mathematics

Both pictures use the same recurrence:

$$z_{n+1} = z_n^2 + c$$

| Picture | Fixed | Varied across pixels | What belongs to the set? |
| --- | --- | --- | --- |
| Mandelbrot | Starting value $z_0 = 0$ | Parameter $c$ | Parameters whose critical orbit stays bounded forever |
| Filled Julia | Parameter $c$ | Starting value $z_0$ | Starting points whose orbits stay bounded forever |

The **Julia set** is the boundary of the filled Julia set. The colored pictures use an escape-time approximation: a pixel's color records how quickly its orbit crosses the escape threshold. Black means it did not escape within the selected iteration budget; a finite render alone does not prove membership.

For $|c| \leq 2$, an orbit that reaches $|z| > 2$ escapes. The Mandelbrot set also provides a parameter map: the Julia set is connected exactly when the critical orbit of `0` remains bounded. It is not a geometric superset containing every Julia set.

Try `c = 0` for the unit-circle Julia boundary or `c = -1` for the basilica pattern. Read the [Mandelbrot–Julia comparison](https://fractal-space-explorer.vercel.app/mandelbrot-vs-julia) and [escape-radius explanation](https://fractal-space-explorer.vercel.app/escape-radius-proof) for worked examples and assumptions.

### “God's set” and the story behind the name

The Mandelbrot set is sometimes called the **“thumbprint of God”** or **“God's set”** as a poetic response to its intricate structure. These are nicknames, not a theorem or scientific evidence for a religious claim. The [Learn page](https://fractal-space-explorer.vercel.app/learn) connects that story with the recurrence, diagrams and proofs.

## Proofs and reference notes

The numbered PDFs correspond to references in the Java comments. The [web proof library](https://fractal-space-explorer.vercel.app/learn) adds explanations and diagrams; the [study handbook](https://fractal-space-explorer.vercel.app/proofs-explained.pdf) collects the expanded material.

| Note | Subject | Source PDF |
| --- | --- | --- |
| 1.1 | Mandelbrot parameter bounds | [Read](Proofs/Mandel%20Brot%20and%20Julia%20set%20Equations/1.1.pdf) |
| 1.2 | Squared magnitude and the parameter-radius test | [Read](Proofs/Mandel%20Brot%20and%20Julia%20set%20Equations/1.2.pdf) |
| 1.3 | Escape condition and triangle inequality | [Read](Proofs/Mandel%20Brot%20and%20Julia%20set%20Equations/1.3.pdf) |
| 1.4 | Squared magnitude of the iterated value | [Read](Proofs/Mandel%20Brot%20and%20Julia%20set%20Equations/1.4.pdf) |
| 1.5 | Real and imaginary iteration equations | [Read](Proofs/Mandel%20Brot%20and%20Julia%20set%20Equations/1.5.pdf) |
| 2.1 | Mapping pixels to the complex plane | [Read](Proofs/UIUX%20equations/2.1.pdf) |
| 2.2 | Centering a zoom on the clicked point | [Read](Proofs/UIUX%20equations/2.2.pdf) |
| 2.3 | Translating the view while dragging | [Read](Proofs/UIUX%20equations/2.3.pdf) |

## Gallery

**Julia set for approximately `c = -0.704029749122184 - 0.26i`**

![Julia set generated by the Java application](https://github.com/user-attachments/assets/101b2052-a57a-41c0-a246-2fe4c7cf8b9f)

**Mandelbrot detail at approximately 9,600× zoom**

![Zoomed Mandelbrot detail](https://github.com/user-attachments/assets/8e8110b2-f301-472c-91b8-a70b0f17e167)

## Repository structure

```text
src/
  Main.java         Swing entry point, controls and Julia window creation
  MandelBrot.java   Mandelbrot calculation, navigation and PNG saving
  Julia.java        Julia calculation, navigation and PNG saving
Proofs/
  Mandel Brot and Julia set Equations/   Notes 1.1–1.5
  UIUX equations/                       Notes 2.1–2.3
out/                Local compilation output
```

## Implementation limits

- Calculations use `double` precision. Deep zoom eventually loses coordinate detail.
- The original Julia renderer skips calculation when `|c| > 2`; its resulting image does not represent the general Julia set for those parameters. Use constants within that bound for this implementation.
- More iterations improve the escape-time approximation but increase compute time. They do not add pixels or make a numerical render a mathematical proof.

## Contributing

Report bugs or mathematical corrections in [Issues](https://github.com/RaghavSharma006/MandelBrot/issues). For a rendering issue, include the complex coordinate, iteration limit, zoom, desktop/web version and a screenshot. For desktop problems, also include your JDK and operating system.

Keep pull requests focused, explain the behavior change and include relevant verification. Changes to equations should state their assumptions and link the corresponding proof or reference.

Created by [Raghav Sharma](https://github.com/RaghavSharma006).
