package team_emergensor.co.jp.emergensor.service.acceleration;

/**
 * Created by koichihasegawa on 2018/07/17.
 */

public class ActionClassifier {

    public enum Action {
        STANDING_POCKET, STANDING_HAND, WALK_NORMALLY_POCKET, WALK_NORMALLY_HAND, WALK_HURRIEDLY_POCKET, WALK_HURRIEDLY_HAND, RUN_NORMALLY_POCKET, RUN_NORMALLY_HAND, RUN_HURRIEDLY_POCKET, RUN_HURRIEDLY_HAND, CROUCH_POCKET, CROUCH_HAND, SHAKE_HAND, UPSTAIRS_NORMALLY_POCKET, UPSTAIRS_NORMALLY_HAND, UPSTAIRS_HURRIEDLY_POCKET, UPSTAIRS_HURRIEDLY_HAND, DOWNSTAIRS_NORMALLY_POCKET, DOWNSTAIRS_NORMALLY_HAND, DOWNSTAIRS_HURRIEDLY_POCKET, DOWNSTAIRS_HURRIEDLY_HAND,;
        private static Action[] values = values();
    }

    /**
     * 実装上、まだすべてのラベルが帰ってくるとは限らない（そもそも階段とかは実験してないから）。
     *
     * @throws Exception
     */
    public static Action classify(double[] feature) throws Exception {
        double res = myClassify(new Object[]{
                feature[0],
                feature[1],
                feature[2],
                feature[3],
                feature[4],
                feature[5],
        });
        return res == Double.NaN ? null : Action.values[(int) res];
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static double myClassify(Object[] i)
            throws Exception {

        double p = Double.NaN;
        p = ActionClassifier.N67b467e90(i);
        return p;
    }

    static double N67b467e90(Object[] i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 10;
        } else if (((Double) i[1]).doubleValue() <= 0.033582722356767514) {
            p = ActionClassifier.N4d1b0d2a1(i);
        } else if (((Double) i[1]).doubleValue() > 0.033582722356767514) {
            p = ActionClassifier.N149494d83(i);
        }
        return p;
    }

    static double N4d1b0d2a1(Object[] i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 10;
        } else if (((Double) i[0]).doubleValue() <= 0.9821716536011561) {
            p = 10;
        } else if (((Double) i[0]).doubleValue() > 0.9821716536011561) {
            p = ActionClassifier.N954b04f2(i);
        }
        return p;
    }

    static double N954b04f2(Object[] i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 1;
        } else if (((Double) i[1]).doubleValue() <= 0.00196438617835204) {
            p = 1;
        } else if (((Double) i[1]).doubleValue() > 0.00196438617835204) {
            p = 11;
        }
        return p;
    }

    static double N149494d83(Object[] i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 1.5153405631718784) {
            p = ActionClassifier.N710726a34(i);
        } else if (((Double) i[0]).doubleValue() > 1.5153405631718784) {
            p = ActionClassifier.N24a3597818(i);
        }
        return p;
    }

    static double N710726a34(Object[] i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 12;
        } else if (((Double) i[2]).doubleValue() <= 18.121929999962077) {
            p = 12;
        } else if (((Double) i[2]).doubleValue() > 18.121929999962077) {
            p = ActionClassifier.N646007f45(i);
        }
        return p;
    }

    static double N646007f45(Object[] i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 9;
        } else if (((Double) i[5]).doubleValue() <= 15.551501902978737) {
            p = ActionClassifier.N481a15ff6(i);
        } else if (((Double) i[5]).doubleValue() > 15.551501902978737) {
            p = ActionClassifier.N4c76260413(i);
        }
        return p;
    }

    static double N481a15ff6(Object[] i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 9;
        } else if (((Double) i[2]).doubleValue() <= 121.35674982346154) {
            p = ActionClassifier.N78186a707(i);
        } else if (((Double) i[2]).doubleValue() > 121.35674982346154) {
            p = ActionClassifier.N77846d2c11(i);
        }
        return p;
    }

    static double N78186a707(Object[] i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 2;
        } else if (((Double) i[1]).doubleValue() <= 0.2536924744943714) {
            p = ActionClassifier.N306279ee8(i);
        } else if (((Double) i[1]).doubleValue() > 0.2536924744943714) {
            p = ActionClassifier.N545997b19(i);
        }
        return p;
    }

    static double N306279ee8(Object[] i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 1.093990981464767) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() > 1.093990981464767) {
            p = 3;
        }
        return p;
    }

    static double N545997b19(Object[] i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 9;
        } else if (((Double) i[4]).doubleValue() <= 23.54141866641898) {
            p = 9;
        } else if (((Double) i[4]).doubleValue() > 23.54141866641898) {
            p = ActionClassifier.N4cf4d52810(i);
        }
        return p;
    }

    static double N4cf4d52810(Object[] i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 5;
        } else if (((Double) i[0]).doubleValue() <= 1.306576786811257) {
            p = 5;
        } else if (((Double) i[0]).doubleValue() > 1.306576786811257) {
            p = 9;
        }
        return p;
    }

    static double N77846d2c11(Object[] i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 7;
        } else if (((Double) i[4]).doubleValue() <= 33.851772805379944) {
            p = 7;
        } else if (((Double) i[4]).doubleValue() > 33.851772805379944) {
            p = ActionClassifier.N548ad73b12(i);
        }
        return p;
    }

    static double N548ad73b12(Object[] i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 9;
        } else if (((Double) i[0]).doubleValue() <= 1.2819805047851724) {
            p = 9;
        } else if (((Double) i[0]).doubleValue() > 1.2819805047851724) {
            p = 7;
        }
        return p;
    }

    static double N4c76260413(Object[] i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 2;
        } else if (((Double) i[3]).doubleValue() <= 70.66210010942854) {
            p = ActionClassifier.N2641e73714(i);
        } else if (((Double) i[3]).doubleValue() > 70.66210010942854) {
            p = 4;
        }
        return p;
    }

    static double N2641e73714(Object[] i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 1.2254362536209271) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() > 1.2254362536209271) {
            p = ActionClassifier.N727803de15(i);
        }
        return p;
    }

    static double N727803de15(Object[] i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 4;
        } else if (((Double) i[2]).doubleValue() <= 102.74450571497583) {
            p = ActionClassifier.N704921a516(i);
        } else if (((Double) i[2]).doubleValue() > 102.74450571497583) {
            p = 2;
        }
        return p;
    }

    static double N704921a516(Object[] i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 2;
        } else if (((Double) i[3]).doubleValue() <= 44.900624777004595) {
            p = ActionClassifier.Ndf27fae17(i);
        } else if (((Double) i[3]).doubleValue() > 44.900624777004595) {
            p = 4;
        }
        return p;
    }

    static double Ndf27fae17(Object[] i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 4;
        } else if (((Double) i[1]).doubleValue() <= 0.4850617356556739) {
            p = 4;
        } else if (((Double) i[1]).doubleValue() > 0.4850617356556739) {
            p = 2;
        }
        return p;
    }

    static double N24a3597818(Object[] i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 6;
        } else if (((Double) i[0]).doubleValue() <= 1.9116119064641535) {
            p = ActionClassifier.N16f7c8c119(i);
        } else if (((Double) i[0]).doubleValue() > 1.9116119064641535) {
            p = 8;
        }
        return p;
    }

    static double N16f7c8c119(Object[] i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 8;
        } else if (((Double) i[5]).doubleValue() <= 47.48261367224747) {
            p = 8;
        } else if (((Double) i[5]).doubleValue() > 47.48261367224747) {
            p = 6;
        }
        return p;
    }

}

