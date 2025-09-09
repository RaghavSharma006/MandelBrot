# Mandelbrot and Julia Set Explorer 🌌

A **Java Swing application** to explore the Mandelbrot set with interactive zooming, panning, and Julia set generation.

---

## About the Mandelbrot Set 🌀

The [Mandelbrot set](https://en.wikipedia.org/wiki/Mandelbrot_set) is one of the most famous **fractal structures** in mathematics, discovered by Benoît Mandelbrot in 1980. It is often referred to as the "**God's set**" because of its infinite complexity and beauty, revealing intricate patterns at every scale.

Mathematically, a point \$c\$ in the complex plane belongs to the Mandelbrot set if the iterative sequence defined by \$z\_{n+1} = z\_n^2 + c\$ (starting with \$z\_0 = 0\$) **does not escape to infinity**.

## About the Julia Set 🌀

The [Julia set](https://en.wikipedia.org/wiki/Julia_set) is another fascinating **fractal structure** closely related to the Mandelbrot set, named after French mathematician Gaston Julia. Each Julia set corresponds to a specific complex number \$c\$, and its shape can vary from connected and smooth to highly fragmented.

Mathematically, a point \$z\_0\$ in the complex plane belongs to the Julia set for a given \$c\$ if the iterative sequence defined by \$z\_{n+1} = z\_n^2 + c\$ **does not escape to infinity**, starting from that \$z\_0\$. Julia sets reveal an incredible diversity of patterns, making them both visually stunning and mathematically rich.

## Mandelbrot & Julia Set Connection 🔗

The Mandelbrot set is more than just a stunning fractal — it is the **superset of all Julia sets**. Every point in the Mandelbrot set corresponds to a unique Julia set.

* If the point lies **inside the Mandelbrot set** (typically black regions), the corresponding Julia set is **connected** — a beautiful, unbroken fractal.
* If the point lies **outside the Mandelbrot set**, the Julia set becomes **disconnected** or "dusty" — fragmented into tiny islands.

The deeper you explore the Mandelbrot set (zooming in), the more complex and varied the Julia sets become. This shows how every Julia set is a reflection of a specific point in the Mandelbrot set, making the Mandelbrot set a map of all possible Julia sets.

---

## Features ✨

* Interactive **Mandelbrot set visualization**

  * Left double-click → zoom in
  * Right double-click → zoom out
  * Drag → pan the view
* Display **real and imaginary coordinates** of clicked points
* Generate **Julia sets** for any point
* Adjustable **zoom factor** and **maximum iterations**
* **Save images** directly to Desktop with 💾 button (customizable path in code)
* Live **zoom level display**
* Smooth, colorful gradient rendering

---

## Screenshots 📸

**Mandelbrot Set**
![Mandelbrot](https://github.com/user-attachments/assets/baa4ccc8-797d-47cd-b664-7d0065606654)

**Julia Set for -0.704029749122184 + -0.26 i**
![Julia](https://github.com/user-attachments/assets/101b2052-a57a-41c0-a246-2fe4c7cf8b9f)

**9600x Zoomed-in Mandelbrot Set**
![Zoomed Mandelbrot](https://github.com/user-attachments/assets/8e8110b2-f301-472c-91b8-a70b0f17e167)

---

## Usage ▶️

1. Clone the repository:

```bash
git clone https://github.com/RaghavSharma006/MandelBrot
```

2. Compile and run:

```bash
javac Main.java MandelBrot.java Julia.java
java Main
```

3. Controls:

* **Zoom field**: set zoom factor (e.g., 1.25)
* **Iterations field**: set max iterations (e.g., 5000)
* **Real / Imaginary fields**: view coordinates of clicked points or enter any complex number
* **Generate button**: generate the Julia set for the entered complex number
* **Save button (💾)**: save the current image to Desktop or a custom path (modify `saveImageToFolder` method in both MandelBrot and Julia classes)
  * Example custom path:
  ```
   desktopPath = "C:\Users\Raghav Sharma\Pictures\Camera Roll";
  ```
* **Drag mouse**: pan across the fractal
* **Left double-click**: zoom in
* **Right double-click**: zoom out

---

## Project Structure 📂

```

MandelBrot/
│
├─ src/
│   ├─ Main.java           # Entry point for the application
│   ├─ MandelBrot.java     # Mandelbrot set panel and logic
│   ├─ Julia.java          # Julia set panel and logic
│
├─ Proofs/
│   ├─ Mandel Brot and Julia set Equations/
│   │   ├─ 1.1.pdf
│   │   ├─ 1.2.pdf
│   │   ├─ 1.3.pdf
│   │   ├─ 1.4.pdf
│   │   ├─ 1.5.pdf
│   ├─ UIUX equations/
│       ├─ 2.1.pdf
│       ├─ 2.2.pdf
│       ├─ 2.3.pdf
│
├─ out/                   # Compiled classes
│   ├─ production/
│       ├─ MandelBrot/
│           ├─ Main.class
│           ├─ MandelBrot.class
│           ├─ Julia.class
│
├─ .gitignore
├─ MandelBrot.iml
├─ project\_structe.txt
└─ ...

```
Notes:

* `src/` contains all Java source files.
* `Proofs/` contains PDFs with mathematical proofs for Mandelbrot, Julia sets, and UI behavior.
* `out/` contains compiled `.class` files.
* Hidden files/folders like `.git/` and `.idea/` are for version control and IDE configuration.

---

## Proofs & Equations 📐

We have included mathematical proofs and derivations for the Mandelbrot and Julia set calculations, as well as for the interactive UI behavior. These are referenced in the code as comments:

* **Mandel Brot and Julia set Equations:**
  * 1.1: [Why c lies between -2 and 2 on the real axis, and -2i and 2i on the imaginary axis](Proofs/Mandel%20Brot%20and%20Julia%20set%20Equations/1.1.pdf)
  * 1.2: [Distance Formula in the Complex Plane (ca * ca) + (cb * cb) > 4](Proofs/Mandel%20Brot%20and%20Julia%20set%20Equations/1.2.pdf)
  * 1.3: [Escape Condition Proof Using Triangle Inequality](Proofs/Mandel%20Brot%20and%20Julia%20set%20Equations/1.3.pdf)
  * 1.4: [Distance Formula for zₙ in the Complex Plane](Proofs/Mandel%20Brot%20and%20Julia%20set%20Equations/1.4.pdf)
  * 1.5: [Mandelbrot and Julia Set Iteration Equations](Proofs/Mandel%20Brot%20and%20Julia%20set%20Equations/1.5.pdf)

* **UI/UX References:**
  * 2.1: [Converting Pixel Coordinates to Complex Plane](Proofs\UIUX%20equations2\.1.pdf)
  * 2.2: [Zoom Window Centring](Proofs\UIUX%20equations2\2.2.pdf)
  * 2.3: [Drag translation](Proofs\UIUX%20equations2\2.3.pdf)

*Full derivations can be found in the [`Proofs/`](Proofs/) folder as PDF files.*
---

## Dependencies 🛠️

* Java JDK 11+ (tested with JDK 23)
* Uses **standard Java Swing & AWT**, no external libraries

---

## Notes 💡

* Saved images are automatically stored on the **Desktop** as `mandelbrot1.png`, `mandelbrot2.png`, etc.
* Zoom and pan operations may take some time on high-resolution screens as calculations are complex.
* Clicking a point updates the Real and Imaginary fields dynamically.
* Julia set generation opens a new JFrame for any selected point.

---

## License 📄
```

MIT License © Raghav Sharma


```
