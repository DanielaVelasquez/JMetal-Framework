package co.edu.unicauca.elm;

import co.edu.unicauca.matrix.util.MatrixUtil;
import co.edu.unicauca.moore_penrose.AbstractMoorePenroseMethod;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import co.edu.unicauca.elm_function.ELMFunction;
import no.uib.cipr.matrix.Matrix;

/**
 * Extreme Learning Machine
 */
public class ELM {

	/**
	 * -----------------------------------------------------------------------------------------
	 * Constants
	 * -----------------------------------------------------------------------------------------
	 */
	/**
	 * Minimum value allowed in ELM's weight
	 */
	private static final double MIN_VALUE = -1;
	/**
	 * Max value allowed in ELM's weight
	 */
	private static final double MAX_VALUE = 1;

	/**
	 * -----------------------------------------------------------------------------------------
	 * Enums
	 * -----------------------------------------------------------------------------------------
	 */
	/**
	 * ELM Type for classification and regression
	 */
	public enum ELMType {

		REGRESSION, CLASSIFICATION
	}

	/**
	 * -----------------------------------------------------------------------------------------
	 * Atributes
	 * -----------------------------------------------------------------------------------------
	 */

	/**
	 * Activation Function
	 */
	private ELMFunction function;
	/**
	 * ELM accuracy while training or training it contains accuracy of the last
	 * activity performed
	 */
	private double accuracy;
	/**
	 * ELM type (Regression or Classification)
	 */
	private ELMType elmType;
	/**
	 * Number of input neurons
	 */
	private int inputNeurons;
	/**
	 * Number of hidden neurons assigned to the ELM
	 */
	private int hiddenNeurons;
	/**
	 * Number of output neurons
	 */
	private int outputNeurons;
	/**
	 * Number of data
	 */
	private int numberData;
	/**
	 * Bias of hidden neurons
	 */
	private DenseVector biasHiddenNeurons;
	/**
	 * Matrix input weight - Weights between input layer and the hidden layer w_ij
	 * is the weight between neuron i (in input layer) and neuron j (in the hidden
	 * layer)
	 */
	private DenseMatrix inputWeight;
	/**
	 * Matrix output weight - Weights between the hidden layer and output layer b_jk
	 * represents the weight between the neuron j (in the hidden layer) and neuron k
	 * (in output layer)
	 */
	private DenseMatrix outputWeight;
	/**
	 * Input data, where every column is a data set row
	 */
	private DenseMatrix xMatriz;
	/**
	 * Output training/testing data
	 */
	private DenseVector yMatriz;
	/**
	 * Tabular output data, represt the output data as a tabular matrix with -1 and
	 * 1 values
	 */
	private DenseMatrix tabular;
	/**
	 * Output vector of the network
	 */
	private DenseVector tVector;
	/**
	 * Method calculates moore-penrose pseudoinverse
	 */
	private AbstractMoorePenroseMethod inverse;

	private boolean trained;

	/**
	 * -----------------------------------------------------------------------------------------
	 * Methods
	 * -----------------------------------------------------------------------------------------
	 */
	/**
	 * Construct an ELM
	 *
	 * @param elmType            Elm type - classification or regression
	 * @param hiddenNeurons      Number of hidden neurons assigned to the ELM
	 * @param activationFunction Type of activation function - Type of activation
	 *                           function: 'sig' for Sigmoidal function 'sin' for
	 *                           Sine function 'hardlim' for Hardlim function
	 *                           'tribas' for Triangular basis function 'radbas' for
	 *                           Radial basis function
	 * @param classes            number of classes
	 * @param inverse            Method for calculation of moore penrose inverse
	 */
	public ELM(ELMType elmType, int hiddenNeurons, ELMFunction activationFunction, int classes,
			AbstractMoorePenroseMethod inverse) {
		this.elmType = elmType;
		this.hiddenNeurons = hiddenNeurons;
		this.function = activationFunction;
		this.outputNeurons = classes;
		this.accuracy = 0;
		this.numberData = 0;
		this.inverse = inverse;
	}

	/**
	 * Train an artificial neural network using ELM algorithm with the input and
	 * output data given, if input weight and/or bias are not defined in the ELM
	 * they will be randomly assigned
	 */
	public void train() {
		trained = false;

		/**
		 * In case the input weights is not defined in the ELM they will be randomly
		 * assigned
		 */
		if (inputWeight == null) {
			inputWeight = MatrixUtil.randomFill(hiddenNeurons, inputNeurons, MIN_VALUE, MAX_VALUE);
		}
		/**
		 * In case the bias of hidden neurosn is not defined in the ELM they will be
		 * randomly assigned
		 */
		if (biasHiddenNeurons == null) {
			biasHiddenNeurons = MatrixUtil.randomFill(hiddenNeurons, MIN_VALUE, MAX_VALUE);
		}

		// Get output matrix from hidden layer
		DenseMatrix hMatrizLocal = calculateH(xMatriz);
		try {
			// MultiplicationMethod.getMoorePenroseInverse(0.000001, H)
			DenseMatrix pinvH = inverse.calculate(hMatrizLocal);
			DenseMatrix transT = new DenseMatrix(numberData, outputNeurons);
			tabular.transpose(transT);

			outputWeight = new DenseMatrix(hiddenNeurons, outputNeurons);
			pinvH.mult(transT, outputWeight);

			DenseMatrix tMatrizLocal = calculateOutput(hMatrizLocal, numberData);
			accuracy = evaluate(tabular, tMatrizLocal);
			trained = true;
		} catch (Exception ex) {
			accuracy = 0;
		}
	}

	/**
	 * Test the artificial neural network with the input and output data given
	 */
	public void test() {
		// Get output matrix from hidden layer
		if (trained) {
			DenseMatrix hMatrixLocal = calculateH(xMatriz);
			DenseMatrix tMatrixLocal = calculateOutput(hMatrixLocal, numberData);
			accuracy = evaluate(tabular, tMatrixLocal);
		} else {
			accuracy = 0;
		}
	}

	/**
	 * Calculate the matrix's output
	 */
	private DenseMatrix calculateOutput(DenseMatrix hMatrix, int numData) {
		DenseMatrix tTempMatriz = new DenseMatrix(numData, outputNeurons);
		hMatrix.mult(outputWeight, tTempMatriz);
		DenseMatrix tTemp = new DenseMatrix(outputNeurons, numData);
		tTempMatriz.transpose(tTemp);
		return tTemp;
	}

	/**
	 * Calculate the output matrix of the hidden layer
	 *
	 * @return the output matrix of the hidden layer
	 */
	private DenseMatrix calculateH(DenseMatrix xMatrix) {
		int numData = xMatrix.numColumns();
		DenseMatrix tempH = new DenseMatrix(hiddenNeurons, numData);
		inputWeight.mult(xMatrix, tempH);

		DenseMatrix biasMatrix = new DenseMatrix(hiddenNeurons, numData);
		for (int i = 0; i < hiddenNeurons; i++) {
			for (int j = 0; j < numData; j++) {
				biasMatrix.set(i, j, biasHiddenNeurons.get(i));
			}
		}

		tempH.add(biasMatrix);

		DenseMatrix hTemp = new DenseMatrix(hiddenNeurons, numData);
		for (int i = 0; i < hiddenNeurons; i++) {
			for (int j = 0; j < numData; j++) {
				hTemp.set(i, j, function.evaluate(tempH.get(i, j)));
			}
		}
		DenseMatrix hMatriz = new DenseMatrix(numData, hiddenNeurons);
		hTemp.transpose(hMatriz);

		return hMatriz;
	}

	/**
	 * Contrast the output of the neural network (T) and a tabular output (Y)
	 * besides creates the output network with the original format
	 *
	 * @param tabular  tabular matrix
	 * @param tNetwork output from the network
	 * @return ELM's accuracy
	 */
	private double evaluate(DenseMatrix tabular, DenseMatrix tNetwork) {
		double accuracyAcum = 0;
		if (elmType == ELMType.CLASSIFICATION) {
			int numCols = tabular.numColumns();
			int errors = 0;
			this.tVector = new DenseVector(numCols);
			/**
			 * Finds the index where the biggest value is in every T column and tabular
			 * column, if the indexes aren't the same, an error is counted
			 */
			for (int j = 0; j < numCols; j++) {
				double maxY = tabular.get(0, j);
				int indexY = 0;

				double maxT = tNetwork.get(0, j);
				int indexT = 0;

				for (int i = 1; i < outputNeurons; i++) {
					if (tabular.get(i, j) > maxY) {
						maxY = tabular.get(i, j);
						indexY = i;
					}

					if (tNetwork.get(i, j) > maxT) {
						maxT = tNetwork.get(i, j);
						indexT = i;
					}
				}

				if (indexY != indexT) {
					errors++;
				}
				// Original format for the output network
				tVector.set(j, indexT);
			}

			accuracyAcum = 1 - ((double) errors / (double) numCols);
		} else {
			accuracyAcum = elmRegression(tNetwork);
		}
		return accuracyAcum;
	}

	public double elmRegression(DenseMatrix tNetwork) {
		/**
		 * Square differences between tabular and output network
		 */
		int numCols = tabular.numColumns();
		this.tVector = new DenseVector(numCols);
		double aux = 0;

		for (int j = 0; j < numCols; j++) {
			double valueY = tabular.get(0, j);
			double valueT = tNetwork.get(0, j);
			aux += Math.pow((valueY - valueT), 2);

			// Original format for the output network
			this.tVector.set(j, valueT);
		}
		/*
		 * Best solution closer to one
		 */
		return 1 / (1 + Math.sqrt(aux / numCols));
	}

	/**
	 * Creates tabular from a output vector
	 *
	 * @param y output vector
	 * @return tabular matrix (fill with 1 and -1)
	 */
	private DenseMatrix tabularOutput(DenseVector y) {
		DenseMatrix yMatrisLocal;
		if (elmType == ELMType.CLASSIFICATION) {
			int n = y.size();
			yMatrisLocal = new DenseMatrix(outputNeurons, n);

			for (int j = 0; j < n; j++) {
				for (int i = 0; i < outputNeurons; i++) {
					if (i == y.get(j)) {
						yMatrisLocal.set(i, j, 1);
					} else {
						yMatrisLocal.set(i, j, -1);
					}
				}
			}
		} else {
			outputNeurons = 1;
			yMatrisLocal = new DenseMatrix(1, y.size());
			for (int i = 0; i < y.size(); i++) {
				yMatrisLocal.set(0, i, y.get(i));
			}
		}
		return yMatrisLocal;
	}

	public DenseMatrix getInputWeight() {
		return inputWeight;
	}

	public void setInputWeight(DenseMatrix inputWeight) {
		this.inputWeight = inputWeight;
		this.inputNeurons = this.inputWeight.numColumns();
	}

	public void setY(DenseVector yMatrix) {
		this.yMatriz = yMatrix;
		this.tabular = tabularOutput(yMatrix);
	}

	public DenseVector getBiasHiddenNeurons() {
		return biasHiddenNeurons;
	}

	public void setBiasHiddenNeurons(DenseVector biasHiddenNeurons) {
		this.biasHiddenNeurons = biasHiddenNeurons;
	}

	public void setX(DenseMatrix xMatrix) {
		this.xMatriz = xMatrix;
		this.numberData = this.xMatriz.numColumns();
		this.inputNeurons = this.xMatriz.numRows();
	}

	public DenseMatrix getX() {
		return xMatriz;
	}

	/**
	 * Returns networks´s output
	 *
	 * @return networks´s output
	 */
	public DenseVector getOutputNetwork() {
		return tVector;
	}

	public double getOuputWightNorm() {
		if (outputWeight != null)
			return outputWeight.norm(Matrix.Norm.Frobenius);
		return Double.MAX_VALUE;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public int getInputNeurons() {
		return inputNeurons;
	}

	public int getHiddenNeurons() {
		return hiddenNeurons;
	}

	public ELMType getElmType() {
		return elmType;
	}

	public void setElmType(ELMType elmType) {
		this.elmType = elmType;
	}

	public void setInputNeurons(int inputNeurons) {
		this.inputNeurons = inputNeurons;
	}
}