/**
 * @license
 * SPDX-License-Identifier: Apache-2.0
 */

export default function App() {
  return (
    <div className="min-h-screen bg-[#f4f6f5] text-[#1e2923] py-10 px-4">
      <div className="max-w-3xl mx-auto bg-white border border-[#dde5e0] border-t-4 border-t-[#2f5d44] rounded-xl p-6 sm:p-8 shadow-xs">
        <div className="border-b border-[#dde5e0] pb-4 mb-6 flex flex-wrap items-center justify-between gap-4">
          <div>
            <h1 className="text-2xl font-bold tracking-wide text-[#243b2e]">
              AZWI — Version 2.0
            </h1>
            <p className="text-sm text-[#5a6860] mt-1">
              Application web Java 17 / Spring Boot 3 / PostgreSQL de suivi de production de lapins
            </p>
          </div>
          <a
            href="/suivi-lapins.zip"
            download="suivi-lapins.zip"
            className="inline-block bg-[#2f5d44] hover:bg-[#244935] text-white font-semibold text-sm px-4 py-2.5 rounded-lg"
          >
            Télécharger suivi-lapins.zip
          </a>
        </div>

        <div className="space-y-4 text-sm leading-relaxed text-[#36453c]">
          <p>
            La version 2.0 est prête dans <code className="font-mono bg-[#f2f6f3] px-1.5 py-0.5 rounded">suivi-lapins/</code> et configurée pour le déploiement direct via le <code className="font-mono bg-[#f2f6f3] px-1.5 py-0.5 rounded">Dockerfile</code> à la racine.
          </p>
          <p>
            Poussez simplement les modifications vers votre dépôt GitHub pour que Render reconstruise et mette à jour l&apos;application sans modifier vos variables d&apos;environnement ni votre base PostgreSQL existante.
          </p>
        </div>
      </div>
    </div>
  );
}
