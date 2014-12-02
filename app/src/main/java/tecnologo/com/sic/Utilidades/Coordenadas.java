package tecnologo.com.sic.Utilidades;

/**
 * Created by Nacho_Local on 23/11/2014.
 */
public class Coordenadas {

     final private static double R_MAJOR = 6378137.0;
    final private static double R_MINOR = 6378137.0;
//    final private static double R_MINOR = 6356752.3142;

    public static double[] toMerc(double x, double y)
    {
        return new double[] {toMercX(x), toMercY(y)};
    }

    private static double toMercX(double lon)
    {
        return R_MAJOR * Math.toRadians(lon);
    }

    private static double toMercY(double lat)
    {
        if (lat > 89.5) {
            lat = 89.5;
        }
        if (lat < -89.5) {
            lat = -89.5;
        }
        double temp = R_MINOR / R_MAJOR;
        double es = 1.0 - (temp * temp);
        double eccent = Math.sqrt(es);
        double phi = Math.toRadians(lat);
        double sinphi = Math.sin(phi);
        double con = eccent * sinphi;
        double com = 0.5 * eccent;
        con = Math.pow(((1.0-con)/(1.0+con)), com);
        double ts = Math.tan(0.5 * ((Math.PI*0.5) - phi))/con;
        double y = 0 - R_MAJOR * Math.log(ts);
        return y;
    }

    public static double[] fromMerc(double x, double y)
    {
        return new double[] { fromMercX(x), fromMercY(y) };
    }

    private static double fromMercY(double y)
    {
        double temp = R_MINOR / R_MAJOR;
        double e = Math.sqrt(1.0 - (temp * temp));
        return Math.toDegrees(phi2(Math.exp(-y/R_MAJOR),e));
    }

    private static double phi2(double ts, double e)
    {
        int N_ITER=15;
        double HALFPI=Math.PI/2;

        double TOL=0.0000000001;
        double eccnth, phi, con, dphi;
        int i;
        eccnth = .5 * e;
        phi = HALFPI - 2. * Math.atan (ts);
        i = N_ITER;
        do
        {
            con = e * Math.sin (phi);
            dphi = HALFPI - 2. * Math.atan (ts * Math.pow((1. - con) / (1. + con), eccnth)) - phi;
            phi += dphi;

        }
        while ( Math.abs(dphi)>TOL && (0 != --i));
        return phi;
    }

    private static double fromMercX(double x)
    {
        return Math.toDegrees(x / R_MAJOR);
    }
}