package org.example;

import java.util.ArrayList;
import java.util.List;

public class Kolejka {

    // ogólne -----------------------------------------
    private double lambda;
    private double mju; //μ ogólna zdolność obsługi
    private double rho; //ρ ogólny współczynnik wykorzystania systemu
    private int m;
    private int N;
    private double r;
    private double c1;
    private double c2;
    // END ogólne -------------------------------------


    // Które wynikają z ogólnych ----------------------
    private double p_0; // Prawdopodobieństwo że kolejka jest pusta
    private List<Double> listaPrawdopodopienstw;
    private double p_odmowy; // Prawdopodobieństwo odmowy
    private double q; // Względna zdolność obsługi
    private double A; // Bezwzględna zdolność obsługi
    private double m0; // Średnia ilość zajętych kanałów obsługi

    // END Które wynikają z ogólnych -------------------

    public Kolejka() {}

    // konstruktor bez funkcji celu
    public Kolejka(double lambda, double mju, int m, int N) {
        this.lambda = lambda;
        this.mju = mju;
        this.m = m;
        this.N = N;

        this.rho = obliczRho();
        this.p_0 = obliczP_0();
        this.listaPrawdopodopienstw = obliczPrawdopodobienstwa();
        this.p_odmowy = obliczP_odmowy();
        this.q = obliczQ();
        this.A = obliczA();
        this.m0 = obliczM0();
    }

    private double obliczM0() {
        return A / mju;
    }

    private double obliczA() {
        return lambda * q;
    }

    private double obliczQ() {
        return  1 - p_odmowy;
    }


    // Konstruktor do funkcji celu
    public Kolejka(double lambda, double mju, double r, double c1, double c2) {
        this.lambda = lambda;
        this.mju = mju;
        this.r = r;
        this.c1 = c1;
        this.c2 = c2;

        this.rho = obliczRho();
        // TODO: 12.06.2023 do wykminienia
    }

    private double obliczP_0() {
        // q1 - iloraz ciągu geometrycznego
        double q1 = rho / m;
        if (q1 == 0) {
            double suma = 0;
            double skladnikDodawania1, skladnikDodawania2, licznik1, licznik2, mianownik1, mianownik2;
            for (int k = 0; k <= m-1; k++) {
                licznik1 = Math.pow(rho, k);
                mianownik1 = silnia(k);

                licznik2 = Math.pow(rho, m) * (N + 1);
                mianownik2 = silnia(m);

                skladnikDodawania1 = licznik1 / mianownik1;
                skladnikDodawania2 = licznik2 / mianownik2;
                suma += skladnikDodawania1 + skladnikDodawania2;
            }
            suma = Math.pow(suma, -1);
            return suma;
        }
        else {
            double suma = 0;
            double skladnikDodawania1, skladnikDodawania2, licznik1, licznik2, mianownik1, mianownik2;
            for (int k = 0; k <= m-1; k++) {
                licznik1 = Math.pow(rho, k);
                mianownik1 = silnia(k);

                licznik2 = Math.pow(rho, m)  * ( 1 - Math.pow(rho / m, N + 1) );
                mianownik2 = silnia(m) * ( 1 - rho / m );

                skladnikDodawania1 = licznik1 / mianownik1;
                skladnikDodawania2 = licznik2 / mianownik2;
                suma += skladnikDodawania1 + skladnikDodawania2;
            }
            suma = Math.pow(suma, -1);
            return suma;
        }
    }

    private List<Double> obliczPrawdopodobienstwa() {
        List<Double> listaPrawdopodobienstwToReturn = new ArrayList<>();
        // dla przedziału: 1 <= k <= m - 1 -----------------------------
        for (int k = 1; k <=  m - 1; k++) {
            double prawdopodobienstwo_k, licznik, mianownik;
            licznik = Math.pow(rho, k) * p_0;
            mianownik = silnia(k);
            prawdopodobienstwo_k = licznik / mianownik;
            // TODO: 12.06.2023 można tutaj zaokrąglić wynik przed dodaniem
            listaPrawdopodobienstwToReturn.add(prawdopodobienstwo_k);
        }

        // dla przedziału: m <= j <= m + N -----------------------------
        for (int j = m; j <=  m + N - 1; j++) {
            double prawdopodobienstwo_j, licznik, mianownik;
            licznik = Math.pow(rho, j) * p_0;
            mianownik = Math.pow(m, j-m) * silnia(m);
            prawdopodobienstwo_j = licznik / mianownik;
            // TODO: 12.06.2023 można tutaj zaokrąglić wynik przed dodaniem
            listaPrawdopodobienstwToReturn.add(prawdopodobienstwo_j);
        }

        return listaPrawdopodobienstwToReturn;

    }

    private double obliczRho() {
        return this.lambda / this.mju;
    }

    private double obliczP_odmowy() {
        double ulamek, licznik, mianownik;
        licznik = Math.pow(rho, m + N) * p_0;
        mianownik = Math.pow(m, N) * silnia(m);
        ulamek = licznik / mianownik;
        return ulamek;
    }

    private static int silnia(int x) {
        if (x == 0) return 1;
        int factorial = 1;

        for(int i = 2; i <= x; i++) {
            factorial *= i;
        }
        return factorial;
    }


}
