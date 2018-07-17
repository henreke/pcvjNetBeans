package processo;

public class PID{

    public int nPID = 0;
    public float setPoint = 0;
    public float P = 0;
    public float I = 0;
    public float D = 0;

    public PID(int nPID, float setPoint, float P, float I, float D){
        this.nPID = nPID;
        this.P = P;
        this.I =I;
        this.D = D;
        this.setPoint = setPoint;
    }

    public void setSetPoint(float setPoint){
    	this.setPoint = setPoint;
    }

    public void setProportional(float proportional){
    	this.P =  proportional;

    }

    public void setIntegral(float integral){
    	this.I = integral;
    }

    public void setDerivative(float derivative){
    	this.D =  derivative;
    }

    public void setNumPID(int nPID){
    	this.nPID = nPID;
    }

    public float getSetPoint(){
    	return setPoint;
    }
    public float getProportional(){
    	return P;
    }
    public float getIntegral(){
    	return I;
    }
    public float getDerivative(){
    	return D;
    }

    public int getNumPID(){
    	return nPID;
    }
}
